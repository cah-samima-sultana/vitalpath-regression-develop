package com.cardinalhealth.vitalpath.utils


public final class DateUtils {

    public final class DateGenerator{

        private DateGenerator() {
        }

        public static long getDate(int startYear=1900, int endYear=2010) {

            GregorianCalendar gc = new GregorianCalendar();

            int year = DateUtils.randBetween(startYear, endYear);

            gc.set(gc.YEAR, year);

            int dayOfYear = DateUtils.randBetween(1, gc.getActualMaximum(gc.DAY_OF_YEAR));

            gc.set(gc.DAY_OF_YEAR, dayOfYear);

    //        System.out.println(gc.get(gc.YEAR) + "-" + (gc.get(gc.MONTH) + 1) + "-" + gc.get(gc.DAY_OF_MONTH));

            return gc.getTimeInMillis()

        }

    }

    static int randBetween(int start, int end) {
        return start + (int)Math.round(Math.random() * (end - start));
    }
}