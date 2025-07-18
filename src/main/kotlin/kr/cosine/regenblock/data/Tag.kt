package kr.cosine.regenblock.data

import com.google.gson.Gson
import kr.cosine.regenblock.extension.RegenBlockGsonBuilder
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import kotlin.reflect.KClass

class Tag(
    private val persistentDataContainer: PersistentDataContainer?
) {
    private var gson = defaultGson

    constructor(
        persistentDataContainer: PersistentDataContainer?,
        gson: Gson
    ) : this(persistentDataContainer) {
        this.gson = gson
    }

    fun findString(key: String): String? {
        return persistentDataContainer?.get(NamespacedKey(key), PersistentDataType.STRING)
    }

    fun getString(key: String, default: String = ""): String {
        return findString(key) ?: default
    }

    fun setString(key: String, value: String) {
        persistentDataContainer?.set(NamespacedKey(key), PersistentDataType.STRING, value)
    }

    fun findString(key: Key): String? {
        return findString(key.key)
    }

    fun getString(key: Key, default: String = ""): String {
        return getString(key.key, default)
    }

    fun setString(key: Key, value: String) {
        setString(key.key, value)
    }

    fun findDouble(key: String): Double? {
        return persistentDataContainer?.get(NamespacedKey(key), PersistentDataType.DOUBLE)
    }

    fun getDouble(key: String, default: Double = 0.0): Double {
        return findDouble(key) ?: default
    }

    fun setDouble(key: String, value: Double) {
        persistentDataContainer?.set(NamespacedKey(key), PersistentDataType.DOUBLE, value)
    }

    fun findDouble(key: Key): Double? {
        return findDouble(key.key)
    }

    fun getDouble(key: Key, default: Double = 0.0): Double {
        return getDouble(key.key, default)
    }

    fun setDouble(key: Key, value: Double) {
        setDouble(key.key, value)
    }

    fun findFloat(key: String): Float? {
        return persistentDataContainer?.get(NamespacedKey(key), PersistentDataType.FLOAT)
    }

    fun getFloat(key: String, default: Float = 0f): Float {
        return findFloat(key) ?: default
    }

    fun setFloat(key: String, value: Float) {
        persistentDataContainer?.set(NamespacedKey(key), PersistentDataType.FLOAT, value)
    }

    fun findFloat(key: Key): Float? {
        return findFloat(key.key)
    }

    fun getFloat(key: Key, default: Float = 0f): Float {
        return getFloat(key.key, default)
    }

    fun setFloat(key: Key, value: Float) {
        setFloat(key.key, value)
    }

    fun findInt(key: String): Int? {
        return persistentDataContainer?.get(NamespacedKey(key), PersistentDataType.INTEGER)
    }

    fun getInt(key: String, default: Int): Int {
        return findInt(key) ?: default
    }

    fun setInt(key: String, value: Int) {
        persistentDataContainer?.set(NamespacedKey(key), PersistentDataType.INTEGER, value)
    }

    fun findInt(key: Key): Int? {
        return findInt(key.key)
    }

    fun getInt(key: Key, default: Int): Int {
        return getInt(key.key, default)
    }

    fun setInt(key: Key, value: Int) {
        setInt(key.key, value)
    }

    fun findLong(key: String): Long? {
        return persistentDataContainer?.get(NamespacedKey(key), PersistentDataType.LONG)
    }

    fun getLong(key: String, default: Long): Long {
        return findLong(key) ?: default
    }

    fun setLong(key: String, value: Long) {
        persistentDataContainer?.set(NamespacedKey(key), PersistentDataType.LONG, value)
    }

    fun findLong(key: Key): Long? {
        return findLong(key.key)
    }

    fun getLong(key: Key, default: Long): Long {
        return getLong(key.key, default)
    }

    fun setLong(key: Key, value: Long) {
        setLong(key.key, value)
    }

    fun <T : Any> findValue(key: String, clazz: KClass<T>): T? {
        val json = findString(key)
        return if (json != null) {
            gson.fromJson(json, clazz.java)
        } else {
            null
        }
    }

    fun <T : Any> getValue(key: String, clazz: KClass<T>): T {
        val json = getString(key)
        return gson.fromJson(json, clazz.java)
    }

    fun <T : Any> setValue(key: String, value: T) {
        val json = gson.toJson(value)
        setString(key, json)
    }

    fun <T : Any> findValue(key: Key, clazz: KClass<T>): T? {
        return findValue(key.key, clazz)
    }

    fun <T : Any> getValue(key: Key, clazz: KClass<T>): T {
        return getValue(key.key, clazz)
    }

    fun <T : Any> setValue(key: Key, value: T) {
        setValue(key.key, value)
    }

    fun <T : Enum<T>> findEnum(key: String, enumClass: KClass<T>): T? {
        return enumClass.java.enumConstants.find { it.name == getString(key) }
    }

    fun <T : Enum<T>> findEnum(key: Key, enumClass: KClass<T>): T? {
        return findEnum(key.key, enumClass)
    }

    fun <T : Enum<T>> setEnum(key: String, value: T) {
        setString(key, value.name)
    }

    fun <T : Enum<T>> setEnum(key: Key, value: T) {
        setEnum(key.key, value)
    }

    fun has(key: String): Boolean {
        return persistentDataContainer?.keys?.contains(NamespacedKey(key)) == true
    }

    fun has(key: Key): Boolean {
        return has(key.key)
    }

    fun remove(key: String) {
        persistentDataContainer?.remove(NamespacedKey(key))
    }

    fun remove(key: Key) {
        remove(key.key)
    }

    private fun NamespacedKey(key: String): NamespacedKey {
        return NamespacedKey("mczone", key)
    }

    private companion object {
        val defaultGson: Gson = RegenBlockGsonBuilder().create()
    }
}