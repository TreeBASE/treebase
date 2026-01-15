# Hibernate ORM vs SQL Schema Analysis

This document provides a comprehensive analysis of discrepancies between the Hibernate ORM data model and the SQL-based PostgreSQL schema in TreeBASE.

## Executive Summary

**Finding**: There are discrepancies between the Hibernate ORM layer (source of truth) and the SQL schema instantiated by the database initialization scripts. However, switching to Hibernate-based schema generation introduces significant issues due to seed data dependencies.

**Key Issues Identified**:
1. **Missing patch in CI/CD**: Patch `0011_increase-citation-column-lengths.sql` was not included in `init_db_uptodate.pg` (now fixed)
2. **Column length mismatches**: Several columns in the SQL schema have different lengths than defined in Hibernate (fixed by patches)
3. **Different initialization paths**: CI/CD and Docker use different initialization approaches
4. **Patch idempotency issues**: Some patches failed when schema already had correct types (now fixed)

**Recommendation**: **Keep the SQL-based schema with patches** (Option A) rather than switching to Hibernate-based generation. See "Test Impact Analysis" section for reasoning.

## Current Architecture

### CI/CD Database Initialization
- Uses `treebase-core/db/schema/init_db_uptodate.pg`
- Applies snapshot `0000_SCHEMA_before_patches_start.sql` + `0000_DATA_before_patches_start.sql`
- Then applies patches 0001 through 0011 sequentially
- **Fixed**: Patch 0011 is now included

### Docker Database Initialization
- Uses `docker-compose.yml` volume mounts
- Applies:
  1. `docker/00-init-roles.sql` - Role initialization
  2. `treebase-core/src/main/resources/TBASE2_POSTGRES_CREATION.sql` - Schema creation
  3. `treebase-core/src/main/resources/initTreebase.sql` - Initial data
  4. `docker/03-migration-hibernate-sequence.sql` - Hibernate sequence migration

### Hibernate Configuration
- `hibernate.hbm2ddl.auto=` (empty/disabled)
- Uses annotation-based mapping (`@Entity`, `@Table`, `@Column`)
- Entities defined in `org.cipres.treebase.domain.*`

## Detailed Schema Discrepancies

### 1. Citation Table

| Column | Hibernate Definition | SQL Schema (snapshot) | SQL Schema (TBASE2_POSTGRES_CREATION) | Patch Applied |
|--------|---------------------|----------------------|---------------------------------------|---------------|
| `title` | VARCHAR(500) | VARCHAR(500) | VARCHAR(500) | - |
| `abstract` | VARCHAR(10000) | VARCHAR(10000) | VARCHAR(10000) | - |
| `keywords` | VARCHAR(1000) | VARCHAR(255) | VARCHAR(1000) | Patch 0011 |
| `journal` | VARCHAR(500) | VARCHAR(255) | VARCHAR(500) | Patch 0011 |

**Notes**: 
- The snapshot has outdated column lengths for `keywords` (255 vs 1000) and `journal` (255 vs 500)
- Patch 0011 fixes these in the snapshot-based initialization
- `TBASE2_POSTGRES_CREATION.sql` already has correct values

### 2. TaxonLabel Table

| Column | Hibernate Definition | SQL Schema (snapshot) | Patch Applied |
|--------|---------------------|----------------------|---------------|
| `linked` | BOOLEAN | BOOLEAN | Patch 0010 |

**Notes**: 
- Earlier SQL had `linked` as `smallint`, but this was fixed by Patch 0010
- Both snapshot (after patches) and TBASE2_POSTGRES_CREATION.sql now use BOOLEAN

### 3. Help Table

| Column | Hibernate Definition | SQL Schema |
|--------|---------------------|------------|
| `tag` | VARCHAR(255) (implicit) | VARCHAR(255) |
| `helptext` | TEXT (LOB, 65536) | TEXT |

**Notes**: Schema matches.

### 4. PasswordResetToken Table

| Column | Hibernate Definition | SQL Schema (Patch 0009) |
|--------|---------------------|------------------------|
| `token_id` | BIGINT (auto-increment) | BIGINT (sequence) |
| `token` | VARCHAR(100), unique, NOT NULL | VARCHAR(100), unique, NOT NULL |
| `user_id` | BIGINT, NOT NULL | BIGINT, NOT NULL, FK |
| `expiry_date` | TIMESTAMP, NOT NULL | TIMESTAMP, NOT NULL |
| `used` | BOOLEAN, NOT NULL | BOOLEAN, NOT NULL |

**Notes**: 
- Hibernate uses `@GeneratedValue(strategy = GenerationType.IDENTITY)`
- SQL uses a sequence - these are compatible in PostgreSQL
- Schema matches structurally

### 5. AnalysisStep Table

| Column | Hibernate Definition | SQL Schema (snapshot) |
|--------|---------------------|----------------------|
| `tb_analysisid` | Not in Hibernate entity | VARCHAR(34) |

**Notes**: The SQL schema has a `tb_analysisid` column that doesn't appear to be mapped in Hibernate. This is a legacy TB1 field.

## Root Cause Analysis

### Why Discrepancies Exist

1. **Dual Maintenance**: The SQL schema and Hibernate annotations are maintained separately
2. **Historical Evolution**: The SQL schema evolved over time with patches while Hibernate annotations were updated independently
3. **Different Base Files**: 
   - `TBASE2_POSTGRES_CREATION.sql` appears more aligned with Hibernate
   - The snapshot approach uses older schema + patches
4. **Missing Patch**: Patch 0011 wasn't added to the patch inclusion list

### The Two Initialization Paths

```
CI/CD Path:
  snapshot → patches (0001-0011) → final schema

Docker Path:
  TBASE2_POSTGRES_CREATION.sql → initTreebase.sql → final schema
```

These paths should produce equivalent schemas but use different mechanisms.

## Recommendations

### Option A: Continue with SQL-Based Schema (RECOMMENDED)
**Pros**:
- Known working approach - all tests pass
- Explicit control over schema
- Migration scripts for production
- Seed data loading order is controlled

**Cons**:
- Dual maintenance burden
- Potential for drift between Hibernate and SQL
- Requires diligent patch management

**Action Items**:
1. ✅ Add patch 0011 to `init_db_uptodate.pg` (DONE)
2. ✅ Make patches idempotent (DONE)
3. Consider creating a new schema snapshot that includes all patches
4. Consider adding `hibernate.hbm2ddl.auto=validate` in production

### Option B: Switch to Hibernate-Based Schema Generation
**Pros**:
- Single source of truth (Hibernate entities)
- No impedance mismatch
- Automatic schema updates with `hbm2ddl.auto=update`

**Cons**:
- ❌ **NOT VIABLE** - 21 test failures/errors (see Test Impact Analysis)
- Requires significant refactoring of seed data loading
- Risk of data loss in production if not carefully managed
- Less control over exact DDL

### Option C: Hybrid Approach
**Strategy**: Use Hibernate for tests/development, SQL for production

**Status**: Not recommended due to:
- Seed data dependency issues
- Would require rewriting test data setup
- Maintenance burden of two approaches

## Test Impact Analysis

### Actual Test Results

#### SQL-Based Schema (Current Approach)
**Result**: ✅ All tests pass
```
Tests run: 301, Failures: 0, Errors: 0, Skipped: 43
BUILD SUCCESS
```

#### Hibernate-Based Schema (`hbm2ddl.auto=create`)
**Result**: ❌ 12 failures, 9 errors
```
Tests run: 301, Failures: 12, Errors: 9, Skipped: 53
BUILD FAILURE
```

**Failed Tests (Missing Seed Data)**:
- `ItemDefinitionDAOTest.testFindByDescription`
- `ItemDefinitionDAOTest.testFindPredefinedItemDefinition`
- `MatrixDAOTest.testfindKindByDescription`
- `MatrixDataTypeDAOTest.testFindByDescription`
- `AlgorithmDAOTest.testFinalAllUniqueAlgorithmDescriptions`
- `StudyStatusDAOTest.testFindStatusInProgress/Published/Ready`
- `PhyloTreeDAOTest.testFindTypeByDescription/findKindByDescription/findQualityByDescription`
- `SubmissionServiceImplTest.testProcessNexusFile`

**Error Tests (Foreign Key Violations)**:
- `EnvironmentTest.testGetGeneratedKey` - null value in column violation
- `EnvironmentTest.testSelectFromInsert` - null value in column violation
- `MatrixServiceImplTest.testAddDelete` - NullPointerException (missing service beans)
- `AnalysisServiceImplTest.testAddDelete` - NullPointerException
- `StudyServiceImplTest.*` - Multiple NullPointerExceptions

### Root Cause Analysis

The test failures with Hibernate-based schema generation are caused by:

1. **Missing Seed Data**: Hibernate creates empty tables. Tests expect pre-populated reference data for:
   - `ItemDefinition` (predefined item definitions)
   - `MatrixDataType` (DNA, RNA, Protein, etc.)
   - `StudyStatus` (In Progress, Ready, Published)
   - `TreeType`, `TreeKind`, `TreeQuality`
   - `Algorithm` (reference algorithm types)
   - `User`, `Person` (test user accounts)

2. **Foreign Key Ordering**: When loading `initTreebase.sql` after Hibernate schema creation:
   - Hibernate creates FK constraints immediately
   - SQL script inserts data in wrong order (e.g., `user` before `person`)
   - Results in FK constraint violations

3. **Schema Already Exists Errors**: 
   - `password_reset_token` table created by Hibernate conflicts with SQL script

### Conclusion

**Hibernate-based schema generation is NOT suitable** for the current test setup because:
1. Tests depend on seed data that must be loaded in a specific order
2. The `initTreebase.sql` script was designed for SQL-based schema creation
3. Significant refactoring of test data setup would be required

**Recommendation**: Continue with **Option A (SQL-Based Schema)** with the following improvements:
1. ✅ Keep patches idempotent (fixed in this PR)
2. ✅ Ensure all patches are included in `init_db_uptodate.pg`
3. Consider adding `hibernate.hbm2ddl.auto=validate` in production to catch drift

### Tests to Monitor

Based on the codebase, these test categories should be monitored:

1. **DAO Tests** (`org.cipres.treebase.dao.*`)
   - Test CRUD operations against database
   - May be affected by constraint differences

2. **Service Tests** (`org.cipres.treebase.service.*`)
   - Test business logic with database
   - Should be unaffected by schema generation method

3. **Domain Tests** (`org.cipres.treebase.domain.*`)
   - Test entity relationships
   - May be affected by cascade/fetch settings

## Implementation Plan

### Phase 1: Fix Immediate Issues (COMPLETED)
1. ✅ Add patch 0011 to `init_db_uptodate.pg`

### Phase 2: Validate Current State
1. Run full test suite with current SQL-based initialization
2. Document any existing test failures

### Phase 3: Test Hibernate-Based Initialization
1. Create test configuration with `hbm2ddl.auto=create`
2. Run tests to identify failures
3. Document failures and root causes

### Phase 4: Implement Chosen Strategy
1. Based on test results, implement Option A, B, or C
2. Update CI/CD configuration
3. Update Docker configuration
4. Update documentation

## Appendix: Column Length Constants

From `org.cipres.treebase.domain.TBPersistable`:

```java
public static final int COLUMN_LENGTH_30 = 30;
public static final int COLUMN_LENGTH_50 = 50;
public static final int COLUMN_LENGTH_100 = 100;
public static final int COLUMN_LENGTH_STRING = 255;
public static final int COLUMN_LENGTH_500 = 500;
public static final int COLUMN_LENGTH_STRING_1K = 1000;
public static final int COLUMN_LENGTH_STRING_NOTES = 2000;
public static final int COLUMN_LENGTH_STRING_MAX = 5000;

public static final int CITATION_TITLE_COLUMN_LENGTH = 500;
public static final int CITATION_ABSTRACT_COLUMN_LENGTH = 10000;
public static final int CITATION_KEYWORDS_COLUMN_LENGTH = 1000;
public static final int CITATION_JOURNAL_COLUMN_LENGTH = 500;
```

These constants define the expected column lengths in Hibernate and should be used as the reference when comparing with SQL schemas.
