/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vertere;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author RobSt
 */
public class MapTest {
    
    public MapTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of map method, of class Map.
     */
    @Test
    public void testMap() throws Exception {
        System.out.println("map");
        LongWritable k1 = null;
        Text v1 = null;
        OutputCollector<Text, IntWritable> oc = null;
        Reporter rprtr = null;
        Map instance = new Map();
        instance.map(k1, v1, oc, rprtr);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
