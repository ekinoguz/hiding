package translator.base;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** JUnit tests for {@link Parameters}. */
@RunWith(JUnit4.class)
public class ParametersTest {
  
  private Parameters param;

  @Before
  public void setUp() throws Exception {
    param = Parameters.initialize();
  }

  @Test
  public void test() {
    assertNotNull(param.apiKey);
    assertFalse(param.apiKey.isEmpty());
  }
}
