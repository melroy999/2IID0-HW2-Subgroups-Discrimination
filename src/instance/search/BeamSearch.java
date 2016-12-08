package instance.search;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Class used for beam searches.
 */
public class BeamSearch {

    /**
     * Get the current time stamp string.
     *
     * @return Time stamp in string representation.
     */
    public static String getCurrentTimeStamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
    }
}
