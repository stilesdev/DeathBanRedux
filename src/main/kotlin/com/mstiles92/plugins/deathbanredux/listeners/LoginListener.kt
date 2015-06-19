/*
 * This document is a part of the source code and related artifacts for
 * DeathBanRedux, an open source Bukkit plugin for hardcore-type servers
 * where players are temporarily banned upon death.
 *
 * http://dev.bukkit.org/bukkit-plugins/deathbanredux/
 * http://github.com/mstiles92/DeathBanRedux
 *
 * Copyright (c) 2015 Matthew Stiles (mstiles92)
 *
 * Licensed under the Common Development and Distribution License Version 1.0
 * You may not use this file except in compliance with this License.
 *
 * You may obtain a copy of the CDDL-1.0 License at
 * http://opensource.org/licenses/CDDL-1.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the license.
 */

package com.mstiles92.plugins.deathbanredux.listeners

import com.mstiles92.plugins.deathbanredux.data.PlayerDataStore
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerLoginEvent
import java.util.Calendar

public class LoginListener : Listener {

    EventHandler fun onPlayerPreLogin(event: AsyncPlayerPreLoginEvent) {
        val data = PlayerDataStore.get(event.getUniqueId())

        if (data == null) {
            event.allow()
            return
        }

        if (data.getUnbanCalendar().after(Calendar.getInstance())) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, "You are banned!")
        } else {
            data.resetBanTime()
            event.allow()
        }
    }

    EventHandler fun onPlayerLogin(event: PlayerLoginEvent) {
        val player = event.getPlayer()
        val data = PlayerDataStore.get(player)

        player.sendMessage("You have ${data.revivalCredits} revival credits remaining.")
    }
}