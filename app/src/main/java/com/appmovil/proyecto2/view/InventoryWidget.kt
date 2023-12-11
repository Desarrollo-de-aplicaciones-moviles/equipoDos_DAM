package com.appmovil.proyecto2.view

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.appmovil.proyecto2.R

class InventoryWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {

        appWidgetIds.forEach { appWidgetId ->
            // Create an Intent to launch ExampleActivity.
            val pendingIntent: PendingIntent = PendingIntent.getActivity(
                context,
                0,
                Intent(context, LoginActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val views: RemoteViews = RemoteViews(
                context.packageName,
                R.layout.inventory_widget
            ).apply {
                setOnClickPendingIntent(R.id.gestionarIcon, pendingIntent)
            }


            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }


}