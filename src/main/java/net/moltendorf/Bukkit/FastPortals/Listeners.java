package net.moltendorf.Bukkit.FastPortals;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.util.Vector;

/**
 * Created by moltendorf on 15/06/29.
 *
 * @author moltendorf
 */
public class Listeners implements Listener {
	@EventHandler
	public void PlayerPortalEventHandler(final PlayerPortalEvent event) {
		event.setCancelled(true);

		final Location               location = event.getFrom();
		final Settings.WorldSettings settings = Settings.getInstance().getWorld(location.getWorld().getName());

		if (settings != null && settings.connection != null) {
			final World world = FastPortals.getInstance().getServer().getWorld(settings.connection);

			if (world != null) {
				final Portal from = Portal.Get(location);

				if (from != null) {
					final Vector vector = from.getVector().clone().multiply(settings.multiplier);
					final Location start = new Location(world, vector.getX(), vector.getY(), vector.getZ());

					final Portal to = Portal.Search(start, settings.radius);

					if (to != null) {
						event.setCancelled(false);
						event.setTo(to.createLocation());
						event.useTravelAgent(false);

						return;
					}

					final Player player = event.getPlayer();

					player.sendMessage("§eGenerating new portals is disabled on this server.");
					player.sendMessage("§ePlease manually create a new portal in the other world.");
				}

				return;
			}
		}

		event.getPlayer().sendMessage("§eThis world is not connected to another world.");
	}

	//@EventHandler
	public void EntityPortalEnterEventHandler(final EntityPortalEnterEvent event) {

	}
}
