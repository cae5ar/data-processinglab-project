package com.pstu.dtl.server.math;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;

import com.pstu.dtl.shared.dto.SeriesDto;

public class OLS {

    public Map<String, List<Double>> calculate(List<SeriesDto> selectedSeries, List<Long> periodsList,
            List<SeriesDto> allValuesList) {
        ArrayList<Long> temp = new ArrayList<Long>();
        // Y
        Array2DRowRealMatrix yMatrix = getY(selectedSeries, periodsList);
        temp.clear();
        temp.addAll(periodsList);
        Array2DRowRealMatrix allValuesMatrix = convertToRealMatrix(allValuesList, temp, true);
        // X
        Array2DRowRealMatrix xMatrix = convertToRealMatrix(selectedSeries, temp, true);
        // X'
        Array2DRowRealMatrix xTransposed = (Array2DRowRealMatrix) xMatrix.transpose();
        // (X'*X)
        Array2DRowRealMatrix xToXTransposed = xTransposed.multiply(xMatrix);
        // (X'*X)^-1
        Array2DRowRealMatrix inverse = (Array2DRowRealMatrix) new LUDecomposition(xToXTransposed).getSolver().getInverse();
        // ((X'*X)^-1) * X'
        Array2DRowRealMatrix inverseToXTransposed = inverse.multiply(xTransposed);
        // ((X'*X)^-1) * X' * Y
        Array2DRowRealMatrix alpha = inverseToXTransposed.multiply(yMatrix);
        List<Double> alphaList = new ArrayList<Double>();
        toDoubleList(alphaList, alpha, false, 0);
        // Y'
        Array2DRowRealMatrix yPrognoz = allValuesMatrix.multiply(alpha);
        List<Double> yPrognozList = new ArrayList<Double>();
        toDoubleList(yPrognozList, yPrognoz, false, 0);
        Map<String, List<Double>> map = new HashMap<String, List<Double>>();
        map.put("Y", yPrognozList);
        map.put("ALPHA", alphaList);
        return map;
    }

    private void toDoubleList(List<Double> list, Array2DRowRealMatrix array, boolean isRow, int rowOrColumnIndex) {
        int length = 0;
        if (isRow) {
            length = array.getColumnDimension();
        }
        else {
            length = array.getRowDimension();
        }
        if (isRow) {
            for (int j = 0; j < length; j++) {
                list.add(array.getData()[rowOrColumnIndex][j]);
            }
        }
        else {
            for (int i = 0; i < length; i++) {
                list.add(array.getData()[i][rowOrColumnIndex]);
            }
        }
    }

    protected Array2DRowRealMatrix convertToRealMatrix(List<SeriesDto> series, List<Long> columns,
            boolean fillFirstColumnByOne) {
        double[][] array = new double[(fillFirstColumnByOne) ? columns.size() : columns.size()][series.size()];
        for (int i = 0; i < columns.size(); i++) {
            int j = 0;
            if (fillFirstColumnByOne) {
                array[i][j++] = 1D;
            }
            for (; j < series.size(); j++) {
                array[i][j] = series.get(i).getValues().get(columns.get(j));
            }
        }
        Array2DRowRealMatrix matrix = new Array2DRowRealMatrix(array);
        return matrix;
    }

    private Array2DRowRealMatrix getY(List<SeriesDto> selectedSeries, List<Long> periodsList) {
        int size = selectedSeries.get(0).getValues().size();
        double[][] array = new double[1][size];
        for (int j = 0; j < size; j++) {
            array[0][j] = selectedSeries.get(0).getValues().get(periodsList.get(j));
        }
        Array2DRowRealMatrix matrix = new Array2DRowRealMatrix(array);
        return matrix;
    }
}
