package com.pstu.dtl.server.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.pstu.dtl.shared.dto.Cluster;
import com.pstu.dtl.shared.dto.SeriesDto;

public class Clusterizator {

    private Integer maxIterations = 100;
    private Integer periodsCount;
    private Integer seriesCount;
    private Integer clusterCount;
    /**
     * distances[ряд][кластер] - расстояние ряда до центра кластера
     */
    private Double[][] distances;
    /**
     * normalizedValues[кол-во рядов][кол-во параметров] - значения рядов после
     * нормализации
     */
    private Double[][] normalizedValues;
    /**
     * seriesCluster[номер ряда] = индекс кластера в centroids
     */
    private Integer[] seriesCluster;
    /**
     * мap <Индекс кластера, индексы принадлежащих ему рядов>
     */
    private Map<Integer, List<Integer>> clusterSeriesMap;
    private int moves;
    private List<Cluster> clusters = new ArrayList<Cluster>();;
    private List<SeriesDto> outSeries = new ArrayList<SeriesDto>();;

    public List<Cluster> doKMeansClustering(List<SeriesDto> seriesList, List<Long> periodList, Integer clusterCount) {
        initFieldAndParams(seriesList, periodList, clusterCount);
        // пока не перестанут двигаться элементы и не превысит число итераций
        for (int iteration = 0; iteration < maxIterations && moves != 0; iteration++) {
            // пересчитать центры кластеров
            for (Entry<Integer, List<Integer>> entry : clusterSeriesMap.entrySet()) {
                List<Integer> clusterSeries = entry.getValue();
                for (int p = 0; p < periodsCount && clusterSeries.size() > 0; p++) {
                    double sumSquares = 0;
                    for (Integer s : clusterSeries) {
                        sumSquares += normalizedValues[s][p];
                    }
                    clusters.get(entry.getKey()).getCentroid()[p] = sumSquares / clusterSeries.size();
                }
            }
            calcDistanceFromSeriesToCentroid();
            // новое распределение по классам
            Integer[] newSeriesCluster = new Integer[seriesCount];
            assignClusters(newSeriesCluster);
            // заново заполним удобный словарь класс-ряды
            refreshClusterSeriesMap(newSeriesCluster);
            // посчитаем движения классов
            calcMoves(newSeriesCluster);
        }
        assignOutSeriesToClusters();
        return clusters;
    }

    private void assignOutSeriesToClusters() {
        for (Entry<Integer, List<Integer>> entry : clusterSeriesMap.entrySet()) {
            Integer key = entry.getKey();
            for (Integer seriesIndex : entry.getValue()) {
                SeriesDto seriesDto = outSeries.get(seriesIndex);
                seriesDto.setToClasterDistance(distances[seriesIndex]);
                clusters.get(key).getSeries().add(seriesDto);
            }
        }
    }

    private void initFieldAndParams(List<SeriesDto> seriesList, List<Long> periodList, Integer clusterCount) {
        this.clusterCount = clusterCount;
        Random r = new Random();
        periodsCount = periodList.size();
        seriesCount = seriesList.size();
        clusters = new ArrayList<Cluster>();

        // генерим случайные значения центров
        for (int c = 0; c < clusterCount; c++) {
            Double[] centroids = new Double[periodsCount];
            for (int p = 0; p < periodsCount; p++) {
                centroids[p] = r.nextDouble();
            }
            clusters.add(new Cluster(centroids));
        }
        // values[кол-во рядов][кол-во параметров] - выбранные значения
        Double[][] values = new Double[seriesCount][periodsCount];
        for (int s = 0; s < seriesCount; s++) {
            for (int p = 0; p < periodList.size(); p++) {
                values[s][p] = seriesList.get(s).getValues().get(periodList.get(p));
            }
        }
        // mins[i] - минимум i-го ряда, maxs[i] - максимум i-го ряда
        Double[] mins = new Double[seriesCount];
        Double[] maxs = new Double[seriesCount];
        for (int s = 0; s < seriesCount; s++) {
            Double[] row = getSortedCopy(values, s);
            maxs[s] = row[row.length - 1];
            mins[s] = row[0];
        }

        normalizedValues = new Double[seriesCount][periodsCount];
        for (int s = 0; s < seriesCount; s++) {
            SeriesDto dto = new SeriesDto(seriesList.get(s).getId(), seriesList.get(s).getName());
            for (int p = 0; p < periodsCount; p++) {
                normalizedValues[s][p] = (values[s][p] - mins[s]) / (maxs[s] - mins[s]);
                dto.getValues().put(periodList.get(p), normalizedValues[s][p]);
            }
            outSeries.add(dto);
        }

        distances = new Double[seriesCount][clusterCount];
        calcDistanceFromSeriesToCentroid();

        seriesCluster = new Integer[seriesCount];
        assignClusters(seriesCluster);

        clusterSeriesMap = new LinkedHashMap<Integer, List<Integer>>();
        for (int c = 0; c < clusterCount; c++) {
            clusterSeriesMap.put(c, new ArrayList<Integer>());
        }
        for (int s = 0; s < seriesCount; s++) {
            int c = seriesCluster[s];
            clusterSeriesMap.get(c).add(s);
        }
        moves = seriesCount;
    }

    private void calcMoves(Integer[] newSeriesCluster) {
        moves = 0;
        for (int s = 0; s < seriesCount; s++) {
            if (seriesCluster[s] != newSeriesCluster[s]) moves++;
        }
        seriesCluster = newSeriesCluster;
    }

    private void refreshClusterSeriesMap(Integer[] newSeriesCluster) {
        for (int c = 0; c < clusterCount; c++) {
            clusterSeriesMap.get(c).clear();
        }
        for (int s = 0; s < seriesCount; s++) {
            int c = newSeriesCluster[s];
            clusterSeriesMap.get(c).add(s);
        }
    }

    private void assignClusters(Integer[] seriesCluster) {
        for (int s = 0; s < seriesCount; s++) {
            int nearCluster = 0;
            for (int c = 1; c < clusterCount; c++)
                if (distances[s][nearCluster] > distances[s][c]) nearCluster = c;
            seriesCluster[s] = nearCluster;
        }
    }

    private Double[] getSortedCopy(Double[][] values, int index) {
        Double[] copyOf = Arrays.copyOf(values[index], values[index].length);
        Arrays.sort(copyOf);
        return copyOf;
    }

    private void calcDistanceFromSeriesToCentroid() {
        for (int s = 0; s < seriesCount; s++) {
            for (int c = 0; c < clusterCount; c++) {
                double sum = 0;
                for (int p = 1; p < periodsCount; p++) {
                    sum = sum + Math.pow(clusters.get(c).getCentroid()[p] - normalizedValues[s][p], 2);
                }
                distances[s][c] = Math.sqrt(sum);
            }
        }
    }

}
