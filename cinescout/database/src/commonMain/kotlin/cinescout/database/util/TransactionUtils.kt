package cinescout.database.util

import app.cash.sqldelight.Transacter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

suspend inline fun <T : Transacter> T.suspendTransaction(
    dispatcher: CoroutineDispatcher,
    crossinline block: T.() -> Unit
) {
    withContext(dispatcher) {
        transaction { block() }
    }
}

suspend inline fun <T : Transacter, R> T.suspendTransactionWithResult(
    dispatcher: CoroutineDispatcher,
    crossinline block: T.() -> R
): R = withContext(dispatcher) {
    transactionWithResult { block() }
}

suspend inline fun <T1 : Transacter, T2 : Transacter, T3 : Transacter> suspendTransaction(
    dispatcher: CoroutineDispatcher,
    t1: T1,
    t2: T2,
    t3: T3,
    crossinline block: () -> Unit
) {
    withContext(dispatcher) {
        t1.transaction {
            t2.transaction {
                t3.transaction {
                    block()
                }
            }
        }
    }
}

suspend inline fun <T1 : Transacter, T2 : Transacter> suspendTransaction(
    dispatcher: CoroutineDispatcher,
    t1: T1,
    t2: T2,
    crossinline block: () -> Unit
) {
    withContext(dispatcher) {
        t1.transaction {
            t2.transaction {
                block()
            }
        }
    }
}
