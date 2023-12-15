package com.appmovil.proyecto2.view

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import androidx.core.content.ContextCompat.startActivity
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

            val views: RemoteViews = RemoteViews(
                context.packageName, R.layout.inventory_widget
            ).apply {
                setOnClickPendingIntent(R.id.visibility, pendingIntentWid(context, "clickWibget"))
                setOnClickPendingIntent(R.id.gestionarIcon, pendingIntentLogin)
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
        val visibilityTotal = sharedPreferences.getBoolean("visibilityTotal", false)
        if (context !== null && action == "clickWibget") {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            if (email !== null) {
                appWidgetManager.getAppWidgetIds(ComponentName(context, javaClass)).forEach {
                    val views: RemoteViews = RemoteViews(
                        context?.packageName, R.layout.inventory_widget
                    ).apply {
                        if(!visibilityTotal){
                            setCharSequence(R.id.txtTotalProductos, "setText", totalInventario)
                            setImageViewResource(R.id.visibility, R.drawable.visibility_off_24)
                        }else{
                            setCharSequence(R.id.txtTotalProductos, "setText", "****")
                            setImageViewResource(R.id.visibility, R.drawable.visibility_24)
                        }

                    }
                    appWidgetManager.updateAppWidget(it, views)
                }
                sharedPreferences.edit().putBoolean("visibilityTotal", !visibilityTotal).apply()
            } else {
                appWidgetManager.getAppWidgetIds(ComponentName(context, javaClass)).forEach {
                    val views: RemoteViews = RemoteViews(
                        context?.packageName, R.layout.inventory_widget
                    ).apply {
                        setCharSequence(R.id.txtTotalProductos, "setText", "****")
                        setImageViewResource(R.id.visibility, R.drawable.visibility_24)

                    }
                    appWidgetManager.updateAppWidget(it, views)
                }
                sharedPreferences.edit().putBoolean("visibilityTotal", !visibilityTotal).apply()
                val loginIntent = Intent(context, LoginActivity::class.java).apply {
                    putExtra("widget", true)
                }
                loginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(loginIntent)
            }
        }
    }

}