package net.moltendorf.Bukkit.FastPortals;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

/**
 * Created by moltendorf on 15/06/29.
 *
 * @author moltendorf
 */
public class Portal {
	public static Portal Get(final Location location) {
		return Get(location.getBlock());
	}

	public static Portal Get(final Block original) {
		Block block;

		// Find part of the portal.
	portal:
		if (original.getType() == Material.PORTAL) {
			block = original;
		} else {
			block = original.getRelative(BlockFace.WEST);

			if (block.getType() == Material.PORTAL) {
				break portal;
			}

			block = original.getRelative(BlockFace.NORTH);

			if (block.getType() == Material.PORTAL) {
				break portal;
			}

			block = original.getRelative(BlockFace.EAST);

			if (block.getType() == Material.PORTAL) {
				break portal;
			}

			block = original.getRelative(BlockFace.SOUTH);

			if (block.getType() == Material.PORTAL) {
				break portal;
			}

			return null;
		}

		Block bottom, lowest, highest;

		// Find the center of the portal.
		bottom = Scan(block, BlockFace.DOWN);
		lowest = Scan(bottom, BlockFace.WEST);

		if (lowest == block) {
			lowest = Scan(bottom, BlockFace.NORTH);

			// Can go either way.
			if (lowest == block) {
				highest = Scan(bottom, BlockFace.EAST);

				// Searched the wrong way every time. Only one way to go now.
				if (highest == block) {
					highest = Scan(bottom, BlockFace.SOUTH);
				}
			} else {
				// Can only go south.
				highest = Scan(bottom, BlockFace.SOUTH);
			}
		} else {
			// Can only go east.
			highest = Scan(bottom, BlockFace.EAST);
		}

		final World world = lowest.getWorld();

		final Vector vector;

		if (lowest != highest) {
			vector = lowest.getLocation().toVector().getMidpoint(highest.getLocation().toVector().clone().add(new Vector(1, 0, 1)));
		} else {
			vector = lowest.getLocation().toVector().clone();
		}

		return new Portal(vector, world);
	}

	protected static Portal Search(final Location location, final int radius) {
		final int   startX = location.getBlockX();
		final int   startZ = location.getBlockZ();
		final World world  = location.getWorld();

		Block block;

		for (int y = 0, maxY = world.getHighestBlockYAt(startX, startZ); y < maxY; ++y) {
			block = world.getBlockAt(startX, y, startZ);

			if (block.getType() == Material.PORTAL) {
				return Get(block);
			}
		}

		for (int max = 1; max < radius; ++max) {
			int x = startX - max;
			int z = startZ - max;

			for (int maxX = startX + max; x < maxX; ++x) {
				for (int y = 0, maxY = world.getHighestBlockYAt(x, z); y < maxY; ++y) {
					block = world.getBlockAt(x, y, z);

					if (block.getType() == Material.PORTAL) {
						return Get(block);
					}
				}
			}

			for (int maxZ = startZ + max; z < maxZ; ++z) {
				for (int y = 0, maxY = world.getHighestBlockYAt(x, z); y < maxY; ++y) {
					block = world.getBlockAt(x, y, z);

					if (block.getType() == Material.PORTAL) {
						return Get(block);
					}
				}
			}

			for (int minX = startX - max; x > minX; --x) {
				for (int y = 0, maxY = world.getHighestBlockYAt(x, z); y < maxY; ++y) {
					block = world.getBlockAt(x, y, z);

					if (block.getType() == Material.PORTAL) {
						return Get(block);
					}
				}
			}

			for (int minZ = startZ - max; z > minZ; --z) {
				for (int y = 0, maxY = world.getHighestBlockYAt(x, z); y < maxY; ++y) {
					block = world.getBlockAt(x, y, z);

					if (block.getType() == Material.PORTAL) {
						return Get(block);
					}
				}
			}
		}

		return null;
	}

	private static Block Scan(Block block, final BlockFace direction) {
		Block previous;

		do {
			previous = block;
			block = previous.getRelative(direction);
		} while (block != null && block.getType() == Material.PORTAL);

		return previous;
	}

	public Vector getVector() {
		return vector;
	}

	public World getWorld() {
		return world;
	}

	private final Vector vector;
	private final World  world;

	private Portal(final Vector vector, final World world) {
		this.vector = vector;
		this.world = world;
	}

	public Location createLocation(final float yaw, final float pitch) {
		return new Location(world, vector.getX(), vector.getY(), vector.getZ(), yaw, pitch);
	}

	public Location createLocation() {
		return new Location(world, vector.getX(), vector.getY(), vector.getZ());
	}
}
