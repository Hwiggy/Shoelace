package me.hwiggy.shoelace.api.database
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.io.Closeable
import java.sql.Connection
import java.sql.SQLException
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

private const val URL_FORMAT = "jdbc:mysql://%s:%d/%s"

class MySQL
/**
 * This class serves as an access controller for MySQL connections
 * Generates a new MySQL controller using the specified connection details
 *
 * @param host     The host of the SQL server
 * @param port     The port of the SQL server
 * @param database The SQL database to use
 * @param username The username to use for SQL authentication
 * @param password The password to use for SQL authentication
 * @author Hunter Wignall
 * @version October 13, 2019
 */
constructor(
    host: String,
    port: Int,
    database: String,
    username: String,
    password: String
) : Closeable {
    /**
     * The [HikariDataSource] connection pool where connections will be derived from
     */
    private val dataSource: HikariDataSource

    companion object {
        /**
         * The [ExecutorService] used for submitting asynchronous connections
         */
        @JvmStatic private val executor = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors() * 2
        )
    }

    /**
     * Connects to the database synchronously and returns a [T] from the SQL operation
     *
     * @param callback  The [<] used to generate the result
     * @param <T>       Generic bound for the type of object returned during this operation
     * @return The object returned from this operation, potentially null
     */
    @Throws(SQLException::class)
    fun <T> connect(
        callback: (Connection) -> T?
    ): T? = dataSource.connection.run {
        val result = callback(this)
        commit()
        result
    }

    /**
     * Connects to the database asynchronously and returns a [T] from the SQL operation
     *
     * @param callback  The [<] used to generate the result
     * @param <T>       Generic bound for the type of object returned during this operation
     * @return The object returned from this operation, potentially null
     */
    @Throws(SQLException::class)
    fun <T> connectAsync(
        callback: (Connection) -> T
    ): CompletableFuture<T?> = CompletableFuture.supplyAsync({ connect(callback) }, executor)

    init {
        val hikariConfig = HikariConfig()
        hikariConfig.jdbcUrl = String.format(URL_FORMAT, host, port, database)
        hikariConfig.username = username
        hikariConfig.password = password
        hikariConfig.connectionTimeout = TimeUnit.MINUTES.toMillis(10)
        hikariConfig.isAutoCommit = false
        dataSource = HikariDataSource(hikariConfig)
    }

    override fun close() = dataSource.close()
}