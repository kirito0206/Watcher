package com.example.watcher.data

data class Region(
    val code: Int,
    val `data`: Data,
    val msg: String,
    val reqId: Long,
    val timestamp: Long
)

data class Data(
    val areaTree: List<AreaTree>,
    val chinaDayList: List<ChinaDay>,
    val chinaTotal: ChinaTotal,
    val lastUpdateTime: String,
    val overseaLastUpdateTime: String
)

data class AreaTree(
    val children: List<Children>,
    val extData: ExtDataXX,
    val id: String,
    val lastUpdateTime: String,
    val name: String,
    val today: TodayXX,
    val total: TotalXX
)
data class ChinaDay(
    val date: String,
    val extData: Any,
    val lastUpdateTime: Any,
    val today: TodayXXX,
    val total: TotalXXX
)

data class ChinaTotal(
    val extData: ExtDataXXX,
    val today: TodayXXXX,
    val total: TotalXXXX
)

data class Children(
    val children: List<ChildrenX>,
    val extData: ExtDataX,
    val id: String,
    val lastUpdateTime: String,
    val name: String,
    val today: TodayX,
    val total: TotalX
)

data class ExtDataXX(
    val incrNoSymptom: Int,
    val noSymptom: Int
)

data class TodayXX(
    val confirm: Int,
    val dead: Int,
    val heal: Int,
    val input: Int,
    val severe: Int,
    val storeConfirm: Int,
    val suspect: Int
)

data class TotalXX(
    val confirm: Int,
    val dead: Int,
    val heal: Int,
    val input: Int,
    val severe: Int,
    val suspect: Int
)

data class ChildrenX(
    val children: List<Any>,
    val extData: ExtData,
    val id: String,
    val lastUpdateTime: String,
    val name: String,
    val today: Today,
    val total: Total
)

data class ExtDataX(
    val noSymptom: Int
)

data class TodayX(
    val confirm: Int,
    val dead: Int,
    val heal: Int,
    val input: Int,
    val severe: Any,
    val storeConfirm: Int,
    val suspect: Any
)

data class TotalX(
    val confirm: Int,
    val dead: Int,
    val heal: Int,
    val input: Int,
    val severe: Int,
    val suspect: Int
)

data class ExtData(
    val incrNoSymptom: Int,
    val noSymptom: Int
)

data class Today(
    val confirm: Int,
    val dead: Int,
    val heal: Int,
    val severe: Any,
    val storeConfirm: Int,
    val suspect: Any
)

data class Total(
    val confirm: Int,
    val dead: Int,
    val heal: Int,
    val severe: Int,
    val suspect: Int
)

data class TodayXXX(
    val confirm: Int,
    val dead: Int,
    val heal: Int,
    val input: Int,
    val severe: Int,
    val storeConfirm: Any,
    val suspect: Int
)

data class TotalXXX(
    val confirm: Int,
    val dead: Int,
    val heal: Int,
    val input: Int,
    val severe: Int,
    val suspect: Int
)

data class ExtDataXXX(
    val incrNoSymptom: Int,
    val noSymptom: Int
)

data class TodayXXXX(
    val confirm: Int,
    val dead: Int,
    val heal: Int,
    val input: Int,
    val severe: Int,
    val storeConfirm: Int,
    val suspect: Int
)

data class TotalXXXX(
    val confirm: Int,
    val dead: Int,
    val heal: Int,
    val input: Int,
    val severe: Int,
    val suspect: Int
)