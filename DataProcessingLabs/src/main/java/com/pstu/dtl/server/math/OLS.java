package com.pstu.dtl.server.math;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;

import com.pstu.dtl.shared.dto.SeriesDto;

public class OLS {

    public List<Double> calculate(List<SeriesDto> series, List<Long> periodsList) {
        ArrayList<Long> temp = new ArrayList<Long>();
        temp.add(periodsList.get(0));
        //Y
        Array2DRowRealMatrix yMatrix = convertToRealMatrix(series, temp, false);
        temp.clear();
        temp.addAll(periodsList);
        //X
        Array2DRowRealMatrix xMatrix = convertToRealMatrix(series, temp, true);
        //X'
        Array2DRowRealMatrix xTransposed = (Array2DRowRealMatrix) xMatrix.transpose();
        //(X'*X)
        Array2DRowRealMatrix xToXTransposed = xTransposed.multiply(xMatrix);
        //(X'*X)^-1
        Array2DRowRealMatrix inverse = (Array2DRowRealMatrix)new LUDecomposition(xToXTransposed).getSolver().getInverse();
        //((X'*X)^-1) * X'
        Array2DRowRealMatrix inverseToXTransposed = inverse.multiply(xTransposed);
        //((X'*X)^-1) * X' * Y
        Array2DRowRealMatrix alpha = inverseToXTransposed.multiply(yMatrix);
        //Y'
        Array2DRowRealMatrix yPrognoz = xMatrix.multiply(alpha);
        return null;
    }

    public OLS() {}

    protected Array2DRowRealMatrix convertToRealMatrix(List<SeriesDto> series, List<Long> columns,
            boolean fillFirstColumnByOne) {
        double[][] array = new double[series.size()][(fillFirstColumnByOne) ? columns.size() : columns.size()];
        for (int i = 0; i < series.size(); i++) {
            int j = 0;
            if (fillFirstColumnByOne) {
                array[i][j++] = 1D;
            }
            for (; j < columns.size(); j++) {
                array[i][j] = series.get(i).getValues().get(columns.get(j));
            }
        }
        Array2DRowRealMatrix matrix = new Array2DRowRealMatrix(array);
        return matrix;
    }
}
