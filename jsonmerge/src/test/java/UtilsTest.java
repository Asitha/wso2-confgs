import com.google.gson.Gson;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class UtilsTest {

    @Test(dataProvider = "input")
    public void testMerge(String input, String output, String expected) {

        Map inputMap = new Gson().fromJson(input, Map.class);
        Map outputMap = new Gson().fromJson(output, Map.class);
        Map expectedMap = new Gson().fromJson(expected, Map.class);
        Utils.mergeMaps(inputMap, outputMap);
        assertMaps(outputMap, expectedMap);
    }

    @DataProvider(name = "input")
    public Object[][] getData() {

        return new Object[][]{
                {"{\"a\": \"b\"}", "{\"b\": \"c\"}", "{\"a\":\"b\",\"b\": \"c\"}"},
                {"{\"a\": \"b\"}", "{\"a\": \"c\"}", "{\"a\": \"c\"}"},
                {"{\"a\": \"b\", \"c\": { \"e\": \"b\"}}", "{\"b\": \"c\", \"c\": {\"d\": \"b\"}}", "{\"a\": \"b\"," +
                        "\"b\": \"c\",\"c\": {\"e\": \"b\", \"d\": \"b\"}}"},
                {"{\"a\": \"b\", \"c\": { \"d\": \"b\"}}", "{\"b\": \"c\", \"c\": {\"d\": \"c\"}}", "{\"a\": \"b\"," +
                        "\"b\": \"c\",\"c\": {\"d\": \"c\"}}"},
                {"{\"a\": \"b\", \"c\": [\"a\",\"b\"]}", "{\"b\": \"c\", \"c\": [\"b\",\"c\"]}", "{\"a\": \"b\"," +
                        "\"b\": " +
                        "\"c\", \"c\": [\"b\",\"c\"]}"}
        };
    }

    public static void assertMaps(Map actual, Map expected) {

        actual.forEach((k, v) -> {
            Assert.assertNotNull(expected.get(k));
            if (v instanceof List){
                Assert.assertEqualsNoOrder(((List) v).toArray(),((List)expected.get(k)).toArray());
            }else if (v instanceof Map){
                assertMaps((Map) v,(Map) expected.get(k));
            }
        });
    }
}

