package com.creeperevents.oggehej.obsidianbreaker.obsidianbreaker;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;

/**
 * Based on Bresenham's line algorithm
 * @author oggehej
 */
public class BlockIntersector {
	public static List<Block> getIntersectingBlocks(Location explosionSource, Location block) {
		List<Block> list = new ArrayList<Block>();
		int i, dx, dy, dz, l, m, n, x_inc, y_inc, z_inc, err_1, err_2, dx2, dy2, dz2;
		int pixelX, pixelY, pixelZ;

		pixelX = explosionSource.getBlockX();
		pixelY = explosionSource.getBlockY();
		pixelZ = explosionSource.getBlockZ();
		dx = block.getBlockX() - explosionSource.getBlockX();
		dy = block.getBlockY() - explosionSource.getBlockY();
		dz = block.getBlockZ() - explosionSource.getBlockZ();
		x_inc = (dx < 0) ? -1 : 1;
		l = Math.abs(dx);
		y_inc = (dy < 0) ? -1 : 1;
		m = Math.abs(dy);
		z_inc = (dz < 0) ? -1 : 1;
		n = Math.abs(dz);
		dx2 = l << 1;
		dy2 = m << 1;
		dz2 = n << 1;

		if ((l >= m) && (l >= n)) {
			err_1 = dy2 - l;
			err_2 = dz2 - l;
			for (i = 0; i < l; i++) {
				list.add(explosionSource.getWorld().getBlockAt(pixelX, pixelY, pixelZ));
				if (err_1 > 0) {
					pixelY += y_inc;
					err_1 -= dx2;
				}
				if (err_2 > 0) {
					pixelZ += z_inc;
					err_2 -= dx2;
				}
				err_1 += dy2;
				err_2 += dz2;
				pixelX += x_inc;
			}
		} else if ((m >= l) && (m >= n)) {
			err_1 = dx2 - m;
			err_2 = dz2 - m;
			for (i = 0; i < m; i++) {
				list.add(explosionSource.getWorld().getBlockAt(pixelX, pixelY, pixelZ));
				if (err_1 > 0) {
					pixelX += x_inc;
					err_1 -= dy2;
				}
				if (err_2 > 0) {
					pixelZ += z_inc;
					err_2 -= dy2;
				}
				err_1 += dx2;
				err_2 += dz2;
				pixelY += y_inc;
			}
		} else {
			err_1 = dy2 - n;
			err_2 = dx2 - n;
			for (i = 0; i < n; i++) {
				list.add(explosionSource.getWorld().getBlockAt(pixelX, pixelY, pixelZ));
				if (err_1 > 0) {
					pixelY += y_inc;
					err_1 -= dz2;
				}
				if (err_2 > 0) {
					pixelX += x_inc;
					err_2 -= dz2;
				}
				err_1 += dy2;
				err_2 += dx2;
				pixelZ += z_inc;
			}
		}
		list.add(explosionSource.getWorld().getBlockAt(pixelX, pixelY, pixelZ));

		return list;
	}
}
