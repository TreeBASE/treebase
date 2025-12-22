# JUnit 4 Migration with Test Skipping Support

## Summary
This PR converts tests from JUnit 3 to JUnit 4 style with proper support for test skipping using `org.junit.Assume` API.

## Changes Made

### 1. JUnit Upgrade
- **Upgraded JUnit**: 3.8.1 → 4.13.2 in `pom.xml`
- **Added Maven Surefire Plugin**: Version 2.22.2 for proper JUnit 4 support

### 2. Test Conversion to JUnit 4 Style
Converted 16 test files from JUnit 3 to JUnit 4:
- Added `@Test` annotations to all test methods
- Added `import org.junit.Test;` to all test files
- Tests now use JUnit 4 test runner properly

### 3. Test Skipping with Assume API
Implemented proper test skipping for database-dependent tests:
- Added `import org.junit.Assume;` 
- Used `Assume.assumeTrue()`, `assumeFalse()`, or `assumeNotNull()` for conditional test execution
- When assumptions fail, tests are properly marked as **SKIPPED** (not PASSED or FAILED)

## Test Patterns

### Pattern 1: "Test requires populated database"
**Before (JUnit 3):**
```java
public void testMethod() {
    Data data = loadData();
    if (data == null) {
        logger.info("Test requires populated database.");
        return;
    }
    // test assertions
}
```

**After (JUnit 4 with Assume):**
```java
@Test
public void testMethod() {
    Data data = loadData();
    Assume.assumeNotNull("SKIPPED: testMethod - No data. Test requires populated database.", data);
    // test assertions
}
```

### Pattern 2: "empty database, test skipped"
**Before (JUnit 3):**
```java
public void testMethod() {
    List<Data> data = queryData();
    if (data != null && !data.isEmpty()) {
        // test assertions
    } else {
        logger.info("test - empty database, test skipped");
    }
}
```

**After (JUnit 4 with Assume):**
```java
@Test
public void testMethod() {
    List<Data> data = queryData();
    Assume.assumeFalse("test - empty database, test skipped", data == null || data.isEmpty());
    // test assertions
}
```

## Files Modified (16 test files)

**DAO Tests:**
- PersonDAOTest.java
- UserDAOTest.java
- MatrixDAOTest.java
- TaxonLabelDAOTest.java

**Domain Tests:**
- RowSegmentTest.java
- SpecimenLabelTest.java
- TaxonLabelTest.java
- PhyloTreeTest.java
- StudyTest.java
- SearchResultsTest.java
- NexmlSearchResultConverterTest.java
- NexmlSerializationTest.java

**Service Tests:**
- MatrixServiceImplTest.java
- StudyServiceImplTest.java
- TaxonLabelServiceImplTest.java
- PhyloTreeServiceImplTest.java

## Test Execution Behavior

### Empty Database (CI/CD Pipeline)
When tests run against an empty database:
- Tests with database checks use `Assume` to skip
- `AssumptionViolatedException` is thrown
- Maven Surefire 2.22.2 catches the exception
- Tests are recorded as **SKIPPED** (yellow) in reports
- No test failures occur

### Populated Database (Production)
- All assumptions pass
- Tests execute normally
- Full test coverage achieved

## Benefits

1. **Proper Skip Reporting**: Tests are now correctly reported as "skipped" instead of "passed" when database is empty
2. **CI/CD Visibility**: Better test reporting in continuous integration dashboards
3. **JUnit 4 Compatibility**: Foundation for future JUnit 5 migration
4. **Explicit Test Requirements**: Clear indication when tests require specific database state
5. **No Breaking Changes**: Tests continue to work with Spring test framework

## Compatibility Notes

- Tests still extend `AbstractTransactionalDataSourceSpringContextTests` (Spring/JUnit 4 compatible)
- JUnit 4.13.2 is fully backward compatible
- Maven Surefire 2.22.2 properly handles both JUnit 3 and JUnit 4 tests
- `@Test` annotations work seamlessly with Spring test context
