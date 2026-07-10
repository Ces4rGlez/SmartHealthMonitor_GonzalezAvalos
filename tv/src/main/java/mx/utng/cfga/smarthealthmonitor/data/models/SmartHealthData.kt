package mx.utng.cfga.smarthealthmonitor.data.models

data class LecturaFC(
    val id: Int,
    val fecha: String,
    val hora: String,
    val bpm: Int,
    val estado: String // "Normal", "Alto", etc.
)

object MockData {
    val fcActual = 78
    val pasosActual = 4250

    val historialFC = listOf(
        LecturaFC(1, "Hoy", "16:00", 78, "Normal"),
        LecturaFC(2, "Hoy", "14:15", 95, "Alto"),
        LecturaFC(3, "Hoy", "11:30", 72, "Normal"),
        LecturaFC(4, "Ayer", "22:10", 68, "Normal"),
        LecturaFC(5, "Ayer", "18:45", 85, "Normal"),
        LecturaFC(6, "Ayer", "08:00", 102, "Alto"),
        LecturaFC(7, "25 May", "20:15", 74, "Normal")
    )
}
