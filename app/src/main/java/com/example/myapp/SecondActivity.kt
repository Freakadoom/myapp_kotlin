@file:Suppress("UNUSED_CHANGED_VALUE")

package com.example.myapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapp.uitel.LoadingItems
import kotlinx.android.synthetic.main.activity_second.*
import org.jetbrains.anko.doAsync
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

private const val URL = "https://api.vk.com/method/wall.get?access_token=a3056eafa3056eafa3056eaf8ea372c664aa305a3056eafc38ec1b33dbc856dced9e7d6&owner_id=-146026097&count=100&filter=owner&extended=1&v=5.103"

class SecondActivity : AppCompatActivity() {

    private lateinit var itemAdapter: ItemAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        initRecyclerView()

        val loading = LoadingItems(this)
        loading.startLoading()
        if(itemAdapter.itemCount > 0) {
            loading.isDismiss()
        }

        parsedJSON(URL)

    }

    private fun parsedJSON(url: String) {
        doAsync {

            val apiResponse = URL(url).readText()


            val response = JSONObject(apiResponse).getJSONObject("response")

            val items = response.getJSONArray("items")

            val length = items.length()

            for(i in 0..length){


                val item = items.getJSONObject(i)
                val itemString = item.toString()
                when {
                    itemString.contains("copy_history") -> {
                        val copyHistory = item.getJSONArray("copy_history").getJSONObject(0)
                        val attachments = copyHistory.getJSONArray("attachments").getJSONObject(0)

                        when (attachments.getString("type")) {
                            "video" -> {
                                val textOut = attachments.getJSONObject("video").getString("title")

                                val imageArray = attachments.getJSONObject("video").getJSONArray("image")

                                val image = images(imageArray).toString()

                                takeItems(image, textOut)

                            }
                            "photo" -> {
                                val text = copyHistory.getString("text")
                                val textOut = text.substring(0, text.indexOf(" ", 160)) + "..."

                                val imageArray = attachments.getJSONObject("photo").getJSONArray("sizes")

                                val image = images(imageArray).toString()

                                takeItems(image, textOut)
                            }
                            "link" -> {
                                val textOut = attachments.getJSONObject("link").getString("title")

                                val imageArray = attachments.getJSONObject("link").getJSONObject("photo").getJSONArray("sizes")

                                val image = images(imageArray).toString()

                                takeItems(image, textOut)
                            }
                        }
                    }
                    itemString.contains("attachments") -> {
                        val attachments = item.getJSONArray("attachments").getJSONObject(0)

                        when (attachments.getString("type")) {
                            "video" -> {
                                val textOut = attachments.getJSONObject("video").getString("title")

                                val imageArray = attachments.getJSONObject("video").getJSONArray("image")

                                val image = images(imageArray).toString()

                                takeItems(image, textOut)
                            }
                            "photo" -> {
                                val text = item.getString("text")
                                val textOut = text.substring(0, text.indexOf(" ", 160)) + "..."

                                val imageArray = attachments.getJSONObject("photo").getJSONArray("sizes")

                                val image = images(imageArray).toString()

                                takeItems(image, textOut)
                            }
                            "link" -> {
                                val textOut = attachments.getJSONObject("link").getString("title")

                                val imageArray = attachments.getJSONObject("link").getJSONObject("photo").getJSONArray("sizes")

                                val image = images(imageArray).toString()

                                takeItems(image, textOut)
                            }
                        }
                    }
                }

            }
        }
    }
    private fun takeItems(img: String, text: String) {
        val itemView = Item(
            text = text,
            imageUrl = img
        )

        runOnUiThread { itemAdapter.addItems(itemView) }
    }

    private fun images(imgArr: JSONArray): String? {

        val imageArrayLength = imgArr.length()
        var image: String? = null

        for (j in 0..imageArrayLength) {
            val img = imgArr.getJSONObject(j)
            val imageWidth = img.getInt("width")
            if (imageWidth in 150..600) {
                image = img.getString("url")
                break
            }
        }
        return image
    }


    private fun initRecyclerView(){
        itemAdapter = ItemAdapter()

        with(itemList) {
            adapter = itemAdapter
            layoutManager = LinearLayoutManager(context)
            this.adapter = itemAdapter
            setHasFixedSize(true)
        }
    }

}
