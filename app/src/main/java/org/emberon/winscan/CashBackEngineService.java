package org.emberon.winscan;
import java.util.*;

public class CashBackEngineService {
    private List<Double> probabilities = new ArrayList<>();
    private List<Integer> values = new ArrayList<>();
    private List<AvgCouponValue> couponList = new ArrayList<>();

    private final int VARIABILITY_IN_COUPONS = 3;
    private final double EPSILON = 1.0e-5;

    enum AvgCouponValue {
        TWENTY(20),
        FIFTY(50),
        HUNDRED(100);

        int faceValue;

        AvgCouponValue(int faceValue) {
            this.faceValue = faceValue;
        }

        public int getFaceValue() {
            return faceValue;
        }
    }

    private Map<AvgCouponValue, Double> frequencyMap;

    private double getTotalFrequency() {
        return Arrays.stream(AvgCouponValue.values()).map((coupon) -> frequencyMap.get(coupon)).reduce(0.0, Double::sum);
    }

    public CashBackEngineService() {
        frequencyMap = new HashMap<>();
        initializeBudget();
        recompute();
    }

    private void initializeBudget() {
        frequencyMap.put(AvgCouponValue.TWENTY, 500.0);
        frequencyMap.put(AvgCouponValue.FIFTY, 100.0);
        frequencyMap.put(AvgCouponValue.HUNDRED, 50.0);
    }

    private void clean() {
        probabilities.clear();
        values.clear();
    }

    private void compute() {
        Arrays.stream(AvgCouponValue.values()).forEach(this::computeFor);
//        Arrays.stream(AvgCouponValue.values()).forEach((coupon) -> {
//            log.info(coupon + " " + frequencyMap.get(coupon));
//        });
//        log.info(probabilities.toString());
//        log.info(values.toString());
//        log.info(getTotalFrequency() + "");
    }

    private void recompute() {
        clean();
        compute();
    }

    private void computeFor(AvgCouponValue coupon) {
        int faceValue = coupon.getFaceValue();

        for (int value = faceValue - VARIABILITY_IN_COUPONS; value <= faceValue + VARIABILITY_IN_COUPONS; value++) {
            Double probability = frequencyMap.get(coupon) / (getTotalFrequency() * (VARIABILITY_IN_COUPONS * 2 + 1));
            values.add(value);
            probabilities.add(probability);
            couponList.add(coupon);
        }
    }

    public int getCashBack() {
        Double rand = Math.random();
        Double current = 0.0;
        int index = 0;
        for (Double value : probabilities) {
            current += value;
            if (current > rand) {
                break;
            }
            index++;
        }
        AvgCouponValue couponValue = couponList.get(index);
        int value = values.get(index);

        double freq = frequencyMap.get(couponValue) - (value * 1.0) / couponValue.getFaceValue();
        freq = (freq < 1.0) ? 0.0 : freq;
        frequencyMap.put(couponValue, freq);
        recompute();
        if (getTotalFrequency() < EPSILON) value = 0;
        return value;
    }
}
