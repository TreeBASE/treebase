# Remaining Test File Updates

## Summary
10 test files remain to be updated to use JUnit 4's `Assume` API for proper test skipping.

## Pattern to Apply
All remaining files follow the same pattern as PersonDAOTest.java and UserDAOTest.java.

### Current Pattern (to be replaced):
```java
if (data != null && !data.isEmpty()) {
    // test logic here
} else {
    if (logger.isInfoEnabled()) {
        logger.info(testName + " - empty database, test skipped");
    }
}
```

### New Pattern (using Assume):
```java
// Add import at top of file:
import org.junit.Assume;

// In test method:
Assume.assumeFalse(testName + " - empty database, test skipped", data.isEmpty());
// OR for null checks:
Assume.assumeNotNull(testName + " - empty database, test skipped", data);

// test logic here (no longer inside if block)
```

## Remaining Files (10 total)

### 1. MatrixDAOTest.java
Path: `treebase-core/src/test/java/org/cipres/treebase/dao/matrix/MatrixDAOTest.java`

### 2. AnalyzedDataDAOTest.java
Path: `treebase-core/src/test/java/org/cipres/treebase/dao/study/AnalyzedDataDAOTest.java`

### 3. StudyDAOTest.java
Path: `treebase-core/src/test/java/org/cipres/treebase/dao/study/StudyDAOTest.java`

### 4. SubmissionDAOTest.java
Path: `treebase-core/src/test/java/org/cipres/treebase/dao/study/SubmissionDAOTest.java`

### 5. PhyloTreeDAOTest.java
Path: `treebase-core/src/test/java/org/cipres/treebase/dao/tree/PhyloTreeDAOTest.java`

### 6. UserTest.java
Path: `treebase-core/src/test/java/org/cipres/treebase/domain/admin/UserTest.java`

### 7. MatrixTest.java
Path: `treebase-core/src/test/java/org/cipres/treebase/domain/matrix/MatrixTest.java`

### 8. NexmlAnalysisConverterTest.java
Path: `treebase-core/src/test/java/org/cipres/treebase/domain/nexus/NexmlAnalysisConverterTest.java`

### 9. NexmlMatrixConverterTest.java
Path: `treebase-core/src/test/java/org/cipres/treebase/domain/nexus/NexmlMatrixConverterTest.java`

### 10. NexmlTreeConverterTest.java
Path: `treebase-core/src/test/java/org/cipres/treebase/domain/nexus/NexmlTreeConverterTest.java`

## Step-by-Step Instructions for Each File

For each file above:

1. **Add import statement** at the top (after other imports):
   ```java
   import org.junit.Assume;
   ```

2. **Find all occurrences** of the pattern "empty database, test skipped":
   ```bash
   grep -n "empty database, test skipped" <filepath>
   ```

3. **For each test method**, replace the if-else pattern:
   - Identify the condition being checked (e.g., `data != null`, `!list.isEmpty()`)
   - Add an `Assume` statement before the test logic
   - Remove the if-else wrapper
   - Ensure test logic is no longer indented inside the if block

4. **Choose the correct Assume method**:
   - `Assume.assumeNotNull(message, object)` - for null checks
   - `Assume.assumeFalse(message, boolean)` - for `.isEmpty()` checks
   - `Assume.assumeTrue(message, boolean)` - for positive conditions

## Example Transformation

### Before:
```java
public void testSomething() {
    String testName = "testSomething";
    
    Data data = loadData();
    
    if (data != null) {
        // test assertions
        assertEquals("expected", data.getValue());
        
        if (logger.isInfoEnabled()) {
            logger.info(testName + " verified");
        }
    } else {
        if (logger.isInfoEnabled()) {
            logger.info(testName + " - empty database, test skipped");
        }
    }
}
```

### After:
```java
public void testSomething() {
    String testName = "testSomething";
    
    Data data = loadData();
    
    Assume.assumeNotNull(testName + " - empty database, test skipped", data);
    
    // test assertions
    assertEquals("expected", data.getValue());
    
    if (logger.isInfoEnabled()) {
        logger.info(testName + " verified");
    }
}
```

## Verification

After updating each file:

1. Ensure the import is added
2. Ensure all early-return patterns are converted
3. Check that test logic is properly unindented
4. Run `grep -c "empty database, test skipped" <filepath>` to verify all occurrences are in Assume statements

## Testing

After all files are updated:
```bash
cd /home/runner/work/treebase/treebase
mvn clean test
```

Look for tests marked as "SKIPPED" in the output instead of just passing with early returns.
