package ru.evotor.framework.payment

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.support.annotation.WorkerThread
import ru.evotor.framework.Utils

/**
 * Интерфейс для получения данных платёжных систем, доступных пользователю смарт-терминала.
 */
@WorkerThread
object PaymentSystemApi {

    const val AUTHORITY = "ru.evotor.evotorpos.paymentSystem"

    @JvmField val BASE_URI = Uri.parse("content://$AUTHORITY")

    /**
     * Возвращает список платёжных систем и соответствующих им аккаунтов, доступных пользователю смарт-терминала.
     *
     * @param context Контекст приложения.
     * @return paymentSystemList Список [платёжных ситем][PaymentSystem] и соответствуюзих им [аккаунтов][PaymentAccount].
     */
    @JvmStatic
    fun getPaymentSystems(context: Context): List<Pair<PaymentSystem, List<PaymentAccount>>> {
        val paymentSystemList = mutableListOf<Pair<PaymentSystem, MutableList<PaymentAccount>>>()

        val cursor: Cursor? = context.contentResolver.query(PaymentSystemTable.URI, null, null, null, null)

        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    val paymentSystem = PaymentSystem(
                            Utils.safeValueOf(PaymentType::class.java, cursor.getString(cursor.getColumnIndex(PaymentSystemTable.COLUMN_PAYMENT_TYPE)), PaymentType.UNKNOWN),
                            cursor.getString(cursor.getColumnIndex(PaymentSystemTable.COLUMN_PAYMENT_SYSTEM_USER_DESCRIPTION)),
                            cursor.getString(cursor.getColumnIndex(PaymentSystemTable.COLUMN_PAYMENT_SYSTEM_ID))
                    )

                    val paymentAccount = PaymentAccount(
                            cursor.getString(cursor.getColumnIndex(PaymentSystemTable.COLUMN_ACCOUNT_USER_DESCRIPTION)),
                            cursor.getString(cursor.getColumnIndex(PaymentSystemTable.COLUMN_ACCOUNT_ID))
                    )

                    var inList = false
                    paymentSystemList.forEach {
                        if (it.first == paymentSystem) {
                            inList = true
                            it.second.add(paymentAccount)
                        }
                    }
                    if (!inList) {
                        paymentSystemList.add(Pair(paymentSystem, mutableListOf(paymentAccount)))
                    }
                }
            } finally {
                cursor.close()
            }
        }

        return paymentSystemList
    }

}