package kr.cosine.regenblock.configuration

import kr.cosine.regenblock.notification.Message
import kr.cosine.regenblock.notification.NotificationType
import kr.cosine.regenblock.notification.Sound
import kr.cosine.regenblock.notification.Title
import kr.hqservice.framework.bukkit.core.extension.colorize
import kr.hqservice.framework.global.core.component.Bean
import kr.hqservice.framework.yaml.config.HQYamlConfiguration

@Bean
class RegenBlockConfiguration(
    private val configuration: HQYamlConfiguration
) {
    val regenDuration get() = configuration.getLong("regen-duration", 20) * 50L

    val inventoryGive get() = configuration.getBoolean("inventory-give")

    fun load() {
        loadNotification()
    }

    private fun loadNotification() {
        configuration.getSection("notification")?.apply {
            getKeys().forEach { notificationTypeText ->
                val notificationType = NotificationType.of(notificationTypeText) ?: return@forEach
                getSection(notificationTypeText)?.apply {
                    val sound = getSection("sound")?.let {
                        Sound(
                            it.getString("name"),
                            it.getDouble("volume").toFloat(),
                            it.getDouble("pitch").toFloat()
                        )
                    }
                    val message = Message(getStringList("messages").map(String::colorize))
                    val title = getSection("title")?.let {
                        Title(
                            it.getString("main").colorize(),
                            it.getString("sub").colorize(),
                            it.getInt("fade-in"),
                            it.getInt("duration"),
                            it.getInt("fade-out")
                        )
                    }
                    notificationType.addNotifications(sound, message, title)
                }
            }
        }
    }

    fun reload() {
        NotificationType.clear()
        configuration.reload()
        load()
    }
}