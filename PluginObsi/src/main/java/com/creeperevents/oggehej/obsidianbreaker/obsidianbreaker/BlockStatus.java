package com.creeperevents.oggehej.obsidianbreaker.obsidianbreaker;

/**
 * Stores damage data concerning one block
 * 
 * @author oggehej
 */
class BlockStatus {
	private float damage = 0;
	private float maxDamage;
	private boolean modified = true;
	private final String blockHash;
	private final String chunkHash;

	/**
	 * An object that contains information about the
	 * damage taken and id it was recently modified.
	 * 
	 * @param damage Current damage
	 */
	BlockStatus(String blockHash, String chunkHash, float maxDamage) {
		this.blockHash = blockHash;
		this.chunkHash = chunkHash;
		this.maxDamage = maxDamage;
	}

	/**
	 * Get current damage to block
	 * 
	 * @return Damage
	 */
	float getDamage() {
		return damage;
	}

	/**
	 * Set current damage to block
	 * 
	 * @param damage Damage
	 */
	void setDamage(float damage) {
		this.damage = damage;
	}

	/**
	 * Check whether the block was recently modified or not
	 * 
	 * @return Recently modified
	 */
	boolean isModified() {
		return modified;
	}

	/**
	 * Set whether the block was recently modified or not
	 * 
	 * @param modified Recently modified
	 */
	void setModified(boolean modified) {
		this.modified = modified;
	}

	/**
	 * Get the maximum amount of damage the block can take
	 * 
	 * @return Max damage
	 */
	float getTotalDurability() {
		return this.maxDamage;
	}

	/**
	 * Get the block hash associated with this object
	 * 
	 * @return Block hash
	 */
	String getBlockHash() {
		return blockHash;
	}

	/**
	 * Get the chunk hash associated with this object
	 * 
	 * @return Chunk hash
	 */
	String getChunkHash() {
		return chunkHash;
	}
}
