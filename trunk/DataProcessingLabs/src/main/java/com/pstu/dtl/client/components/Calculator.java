package com.pstu.dtl.client.components;

import java.util.Collection;
import java.util.Iterator;

public class Calculator {

    /**
     * Метод вычисляющий кореляцию
     * 
     * @param s1 - первая коллекция чисел
     * @param s2 - вторая коллекция чисел
     * @param length - количество значений для подсчета кореляции
     * @return коэффицент корелляции
     */
    public static Double caclucalteCorrelation(Collection<Double> s1, Collection<Double> s2, int length) {
        double avg1, avg2, sum1 = 0, sum2 = 0, sgmSum1 = 0, sgmSum2 = 0, sgm1, sgm2, correlNum = 0, correlDenom;
        double[] r1 = new double[length];
        double[] r2 = new double[length];
        Iterator<Double> iterator1 = s1.iterator();
        Iterator<Double> iterator2 = s2.iterator();
        int index = 0;
        while (iterator1.hasNext() && index < length) {
            Double next = iterator1.next();
            Double next2 = iterator2.next();
            r1[index] = next;
            r2[index++] = next2;
            sum1 += next;
            sum2 += next2;
        }
        avg1 = sum1 / length;
        avg2 = sum2 / length;

        for (int i = 0; i < length; i++) {
            correlNum += (r1[i] - avg1) * (r2[i] - avg2);
            sgmSum1 += Math.pow((double) r1[i] - avg1, 2D);
            sgmSum2 += Math.pow((double) r2[i] - avg2, 2D);
        }

        sgm1 = Math.sqrt(sgmSum1);
        sgm2 = Math.sqrt(sgmSum2);
        correlDenom = sgm1 * sgm2;
        return correlNum / correlDenom;
    }
}
