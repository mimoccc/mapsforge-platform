/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fub.utilsmodule.text;

import java.awt.FontMetrics;
import java.util.List;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Serdar
 */
public class StringUtilsTest {

    private static final Logger LOG = Logger.getLogger(StringUtilsTest.class.getName());

    public StringUtilsTest() {
    }

    /**
     * Test of wrapString method, of class StringUtils.
     */
    @Test
    public void testWrapString() {
        LOG.info("wrapString");
        String str = "This filter is responsible to check whether the time difference between each pair of gps points contained by a gps track does not exceed a given threshold. if there is a pair of gps point where the time difference exceed the specified the track get seperated into two segement and the filter continues the filtering process with the second segment until the end of the gps track is reached.";
        int maxWidth = 60;
        LOG.info(StringUtils.wrapString(str, maxWidth));
    }
}
