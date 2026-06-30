/**
 * 
 */
package com.pms.util;

/**
 * 
 */
	
	public class SnowflakeIdGenerator {

	    // Custom epoch (Jan 1, 2023)
	    private static final long EPOCH = 1672531200000L;

	    // Bit allocations
	    private static final long MACHINE_ID_BITS = 5L;
	    private static final long DATACENTER_ID_BITS = 5L;
	    private static final long SEQUENCE_BITS = 12L;

	    // Max values
	    private static final long MAX_MACHINE_ID = ~(-1L << MACHINE_ID_BITS);
	    private static final long MAX_DATACENTER_ID = ~(-1L << DATACENTER_ID_BITS);

	    // Bit shifts
	    private static final long MACHINE_ID_SHIFT = SEQUENCE_BITS;
	    private static final long DATACENTER_ID_SHIFT = SEQUENCE_BITS + MACHINE_ID_BITS;
	    private static final long TIMESTAMP_SHIFT =
	            SEQUENCE_BITS + MACHINE_ID_BITS + DATACENTER_ID_BITS;

	    private final long machineId;
	    private final long datacenterId;

	    private long sequence = 0L;
	    private long lastTimestamp = -1L;

	    // Constructor
	    public SnowflakeIdGenerator(long datacenterId, long machineId) {

	        if (machineId > MAX_MACHINE_ID || machineId < 0) {
	            throw new IllegalArgumentException(
	                    "Machine ID must be between 0 and " + MAX_MACHINE_ID);
	        }

	        if (datacenterId > MAX_DATACENTER_ID || datacenterId < 0) {
	            throw new IllegalArgumentException(
	                    "Datacenter ID must be between 0 and " + MAX_DATACENTER_ID);
	        }

	        this.machineId = machineId;
	        this.datacenterId = datacenterId;
	    }

	    // Generate next ID
	    public synchronized long nextId() {

	        long timestamp = currentTime();

	        if (timestamp < lastTimestamp) {
	            throw new RuntimeException(
	                    "Clock moved backwards. Refusing to generate ID.");
	        }

	        if (timestamp == lastTimestamp) {
	            sequence = (sequence + 1) & ((1L << SEQUENCE_BITS) - 1);

	            if (sequence == 0) {
	                timestamp = waitNextMillis(lastTimestamp);
	            }
	        } else {
	            sequence = 0L;
	        }

	        lastTimestamp = timestamp;

	        return ((timestamp - EPOCH) << TIMESTAMP_SHIFT)
	                | (datacenterId << DATACENTER_ID_SHIFT)
	                | (machineId << MACHINE_ID_SHIFT)
	                | sequence;
	    }

	    private long waitNextMillis(long lastTimestamp) {
	        long timestamp = currentTime();
	        while (timestamp <= lastTimestamp) {
	            timestamp = currentTime();
	        }
	        return timestamp;
	    }

	    private long currentTime() {
	        return System.currentTimeMillis();
	    }
	}
