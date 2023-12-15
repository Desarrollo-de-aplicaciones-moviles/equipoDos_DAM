package com.appmovil.proyecto2.view

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import com.appmovil.proyecto2.R

class InventoryWidget : AppWidgetProvider() {
    private fun updateWidgets(context: Context) {
        val manager = AppWidgetManager.getInstance(context)
        val ids = manager.getAppWidgetIds(ComponentName(context, javaClass))

    }

    private fun pendingIntentWid(
        context: Context?, action: String
    ): PendingIntent? {
        val intentWib = Intent(context, javaClass)
        intentWib.action = action

        return PendingIntent.getBroadcast(
            context, 0, intentWib, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    override fun onUpdate(
        context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray
    ) {
        val sharedPreferences = context.getSharedPreferences("shared", Context.MODE_PRIVATE)

        val visibilityTotal = sharedPreferences.getBoolean("visibilityTotal", false)
        val invisibilityTotal = sharedPreferences.getBoolean("invisibilityTotal", false)
        val totalInventario = sharedPreferences.getString("totalInventario", "0")
        appWidgetIds.forEach { appWidgetId ->
            val pendingIntentLogin: PendingIntent = PendingIntent.getActivity(
                context,
                0,
                Intent(context, LoginActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val pendingIntentHome: PendingIntent = PendingIntent.getActivity(
                context,
                0,
                Intent(context, HomeActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val views: RemoteViews = RemoteViews(
                context.packageName, R.layout.inventory_widget
            ).apply {
                setOnClickPendingIntent(R.id.visibility, pendingIntentWid(context, "clickWibget"))
                setOnClickPendingIntent(R.id.gestionarIcon, pendingIntentLogin)
                //setCharSequence(R.id.gestionarText, "setText", viewModel.obtenerTotalProductos().toString())

            }


            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }


    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        val action = intent!!.action ?: ""
        val sharedPreferences = context!!.getSharedPreferences("shared", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", null)
        val totalInventario = sharedPreferences.getString("totalInventario", "0") ?: "0"
        if (context !== null && action == "clickWibget") {

            if (email !== null) {
                Log.d("mensaLog", "este es el email: " + email)
                val appWidgetManager = AppWidgetManager.getInstance(context)
                appWidgetManager.getAppWidgetIds(ComponentName(context, javaClass)).forEach {
                    val views: RemoteViews = RemoteViews(
                        context?.packageName, R.layout.inventory_widget
                    ).apply {
                        setCharSequence(R.id.txtTotalProductos, "setText", totalInventario)
                    }
                    appWidgetManager.updateAppWidget(it, views)
                }
            } else {

            }
        }
    }

}