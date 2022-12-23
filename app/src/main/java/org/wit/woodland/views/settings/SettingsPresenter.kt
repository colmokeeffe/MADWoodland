package org.wit.woodland.views.settings


import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.wit.woodland.views.BasePresenter
import org.wit.woodland.views.BaseView
import org.wit.woodland.views.VIEW

class SettingsPresenter(view: BaseView) : BasePresenter(view) {


   // lateinit var pieChart: PieChart


    fun doShowStats()
    {
        doAsync{
            val countWoodlands = app.woodlands.findAll().size
            val countVisited = app.woodlands.countVisited()
            val countConifer = app.woodlands.countConifer()
            val countMixed = app.woodlands.countMixed()
            val countBroadleaf = app.woodlands.countBroadleaf()
            var statsString = ""
            if (countWoodlands > 0) {
                val percentage = (countVisited * 100) / countWoodlands
                val percentageConifer = (countConifer* 100) / countWoodlands
                val percentageMixed = (countMixed* 100) / countWoodlands
                val percentageBroadleaf = (countBroadleaf* 100) / countWoodlands
                statsString = """   Total Sum of Woodlands Logged: $countWoodlands 
            |
            |   Number of Woodlands Visited: $countVisited
            |   Percentage of Woodlands Visited: $percentage%
            |
            |   Number of Conifer Woods Logged: $countConifer
            |   Percentage of Conifer Woods: $percentageConifer%
            |
            |   Number of Mixed Woods Logged: $countMixed
            |   Percentage of Mixed Woods: $percentageMixed%
            |
            |   Number of Broadleaf Woods Logged: $countBroadleaf
            |   Percentage of Broadleaf Woods: $percentageBroadleaf%
        """.trimMargin()
            }
            else
            {
                statsString = """Total Woodlands: ${countWoodlands} 
                |Number Visited: ${countVisited}
            """.trimMargin()
            }
            uiThread { view?.showStats(statsString) }
        }
    }

    fun doAddWoodland()
    {
        view?.navigateTo(VIEW.WOODLAND)
    }

    fun doLogout()
    {
        FirebaseAuth.getInstance().signOut()
        app.woodlands.clear()
        view?.navigateTo(VIEW.LOGIN)
    }

    /*fun doShowPieChart(){
        pieChart.setUsePercentValues(true)
        pieChart.getDescription().setEnabled(false)
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)
        pieChart.setDragDecelerationFrictionCoef(0.95f)
        pieChart.setDrawHoleEnabled(true)
        pieChart.setHoleColor(Color.WHITE)
        pieChart.setTransparentCircleColor(Color.WHITE)
        pieChart.setTransparentCircleAlpha(110)
        pieChart.setHoleRadius(58f)
        pieChart.setTransparentCircleRadius(61f)
        pieChart.setDrawCenterText(true)
        pieChart.setRotationAngle(0f)
        pieChart.setRotationEnabled(true)
        pieChart.setHighlightPerTapEnabled(true)
        pieChart.animateY(1400, Easing.EaseInOutQuad)
        pieChart.legend.isEnabled = false
        pieChart.setEntryLabelColor(Color.WHITE)
        pieChart.setEntryLabelTextSize(12f)
        val entries: ArrayList<PieEntry> = ArrayList()
        entries.add(PieEntry(70f))
        entries.add(PieEntry(20f))
        entries.add(PieEntry(10f))
        val dataSet = PieDataSet(entries, "Mobile OS")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f
        pieChart.setData(data)
        pieChart.highlightValues(null)
        pieChart.invalidate()
    }

    private fun generateExcel() {
        try {
            val workbook = HSSFWorkbook()
            val spreadSheet = workbook.createSheet("Sell Report")

            /*Header "Sampe..."*/
            val rowA = spreadSheet.createRow(0)
            val cellAA = rowA.createCell(0)
            cellAA.setCellValue(HSSFRichTextString("Sampe"))
            cellAA.setCellStyle(
                workbook.createCellStyle().apply {
                    alignment = CellStyle.ALIGN_CENTER
                    wrapText = true
                    setFont(workbook.createFont().apply {
                    })
                })

            spreadSheet.addMergedRegion(CellRangeAddress(
                rowA.rowNum,
                rowA.rowNum,
                0,
                /*index start from 0 and 1 additional as there will be total block for total qty per user... */
                products.size + 1
            ))

            vehicles.forEachIndexed { vehicleIdx, vehicleBean ->
                /*first vehicleDetailRow + index (0...N) * 2*/
                val vehicleDetailRow = spreadSheet.createRow(2 + vehicleIdx * (users.size + 4))
                val vehicleDetailCell = vehicleDetailRow.createCell(0)
                vehicleDetailCell.setCellValue(HSSFRichTextString("${vehicleBean.vehicle_no} (${vehicleBean.driver_name})"))
                ExcelStyleUtils.applyCenterAlignedBold(workbook, vehicleDetailCell)
                spreadSheet.addMergedRegion(CellRangeAddress(
                    vehicleDetailRow.rowNum,
                    vehicleDetailRow.rowNum,
                    0,
                    products.size + 1
                ))

                val productDetailsRow = spreadSheet.createRow(vehicleDetailRow.rowNum + 1)
                products.forEachIndexed { productIdx, productBean ->
                    val productDetailsCell = productDetailsRow.createCell(1 + productIdx)
                    productDetailsCell.setCellValue(HSSFRichTextString(productBean.product_code))
                    ExcelStyleUtils.applyCenterAlignedBold(workbook, productDetailsCell)
                    spreadSheet.setColumnWidth(productDetailsCell.columnIndex, 3000)
                }

                /*Total cell at the end of product list...*/
                val totalQtyPerUserCell = productDetailsRow.createCell(products.size + 1)
                totalQtyPerUserCell.setCellValue(HSSFRichTextString("TOTAL"))
                ExcelStyleUtils.applyCenterAlignedBold(workbook, totalQtyPerUserCell)
                spreadSheet.setColumnWidth(totalQtyPerUserCell.columnIndex, 3000)

                users.forEachIndexed { userIdx, userBean ->
                    val userDetailsRow = spreadSheet.createRow(productDetailsRow.rowNum + 1 + userIdx)
                    val userDetailsCell = userDetailsRow.createCell(0)
                    userDetailsCell.setCellValue(HSSFRichTextString("${userBean.firm_name.capitalize()} (${userBean.full_name})"))
                    ExcelStyleUtils.applyCenterAlignedBold(workbook, userDetailsCell)
                    spreadSheet.setColumnWidth(userDetailsCell.columnIndex, 8000)

                    val productQtyMap = HashMap<String, Int>()
                    orders.filter {
                        it.vehicle_id == vehicleBean.id && it.userId == userBean.id
                    }
                        .forEach {
                            it.items.forEach {
                                productQtyMap[it.product_id] = (productQtyMap[it.product_id]
                                    ?: 0).plus(it.qty)
                            }
                        }


                    products.forEachIndexed { productIdx, productBean ->
                        productQtyMap[productBean.id]?.let {
                            val productQtyPerUserCell = userDetailsRow.createCell(1 + productIdx)
                            productQtyPerUserCell.setCellValue(it.toDouble())
                            ExcelStyleUtils.applyCenterAligned(workbook, productQtyPerUserCell)
                            spreadSheet.setColumnWidth(productQtyPerUserCell.columnIndex, 3000)
                        }
                    }

                    val totalProductQtyPerUserCell = userDetailsRow.createCell(products.size + 1)
                    totalProductQtyPerUserCell.cellType = HSSFCell.CELL_TYPE_FORMULA
                    totalProductQtyPerUserCell.cellFormula = "SUM(${CellReference.convertNumToColString(1)}${totalProductQtyPerUserCell.row.rowNum + 1}:${CellReference.convertNumToColString(products.size)}${totalProductQtyPerUserCell.row.rowNum + 1})"
                    ExcelStyleUtils.applyCenterAlignedBold(workbook, totalProductQtyPerUserCell)
                    spreadSheet.setColumnWidth(totalProductQtyPerUserCell.columnIndex, 3000)
                }

                /*Total cell at the end of user list...*/
                val totalQtyPerProductRow = spreadSheet.createRow(productDetailsRow.rowNum + users.size + 1)
                val totalQtyPerProductCell = totalQtyPerProductRow.createCell(0)
                totalQtyPerProductCell.setCellValue(HSSFRichTextString("TOTAL"))
                ExcelStyleUtils.applyCenterAlignedBold(workbook, totalQtyPerProductCell)
                spreadSheet.setColumnWidth(totalQtyPerProductCell.columnIndex, 8000)

                products.forEachIndexed { productIdx, productBean ->
                    val totalProductQtyForAllUserCell = totalQtyPerProductRow.createCell(1 + productIdx)
                    totalProductQtyForAllUserCell.cellType = HSSFCell.CELL_TYPE_FORMULA
                    /*note rowNum is -1 the original excel sheet representaion as it is 0-Based index...*/
                    totalProductQtyForAllUserCell.cellFormula = "SUM(${CellReference.convertNumToColString(totalProductQtyForAllUserCell.columnIndex)}${productDetailsRow.rowNum + 2}:${CellReference.convertNumToColString(totalProductQtyForAllUserCell.columnIndex)}${productDetailsRow.rowNum + users.size + 1})"
                    ExcelStyleUtils.applyCenterAlignedBold(workbook, totalProductQtyForAllUserCell)
                    spreadSheet.setColumnWidth(totalProductQtyForAllUserCell.columnIndex, 3000)
                }

            }
            var fos: FileOutputStream? = null
            fos.use {
                try {
                    val str_path = Environment.getExternalStorageDirectory().toString()
                    val file: File
                    file = File(str_path, getString(R.string.app_name) + ".xls")
                    fos = FileOutputStream(file)
                    workbook.write(fos)
                    Toast.makeText(context, "Excel Sheet Generated:  ${file.path}", Toast.LENGTH_LONG).show()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }*/

    fun doShowWoodlandsMap()
    {
        view?.navigateTo(VIEW.MAP)
    }

    /*fun loadWoodlands()
    {
        doAsync{
            val woodlands = app.woodlands.findAll()
            uiThread{
                view?.showWoodlands(woodlands)
            }
        }
    }
     */

    fun homeView()
    {
        view?.navigateTo(VIEW.LIST)
    }

    fun doShowFavourites()
    {
        view?.navigateTo(VIEW.FAVOURITES)
    }

    fun doSettings()
    {
        view?.navigateTo(VIEW.SETTINGS)
    }

    fun doCancel()
    {
        view?.finish()
    }
}