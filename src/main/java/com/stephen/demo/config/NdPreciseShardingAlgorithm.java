package com.stephen.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

/**
 * 自定义分片规则
 */
@Slf4j
// public class NdShardingConfig implements PreciseShardingAlgorithm, RangeShardingAlgorithm<Integer> {
public class NdPreciseShardingAlgorithm implements PreciseShardingAlgorithm<Integer> {

    @Override
    public String doSharding(Collection availableTargetNames, PreciseShardingValue shardingValue) {
        String target = shardingValue.getValue().toString();
        return shardingValue.getLogicTableName() + "_" + target.substring(0,4);
    }

    // @Override
    // public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<Integer> shardingValue) {
    //     Collection<String> availables = new ArrayList<>();
    //     Range valueRange = shardingValue.getValueRange();
    //     for (String target : availableTargetNames) {
    //         Integer shardValue = Integer.parseInt(target.substring(0,4));
    //         if (valueRange.hasLowerBound()) {
    //             String lowerStr = valueRange.lowerEndpoint().toString();
    //             Integer start = Integer.parseInt(lowerStr.substring(0, 4));
    //             if (start - shardValue > 0) {
    //                 continue;
    //             }
    //         }
    //         if (valueRange.hasUpperBound()) {
    //             String upperStr = valueRange.upperEndpoint().toString();
    //             Integer end = Integer.parseInt(upperStr.substring(0, 4));
    //             if (end - shardValue < 0) {
    //                 continue;
    //             }
    //         }
    //         availables.add(target);
    //     }
    //     return availables;
    // }
}