package org.toshiaki.aoc.day16

fun main() {
    val input = "005473C9244483004B001F79A9CE75FF9065446725685F1223600542661B7A9F4D001428C01D8C30C61210021F0663043A20042616C75868800BAC9CB59F4BC3A40232680220008542D89B114401886F1EA2DCF16CFE3BE6281060104B00C9994B83C13200AD3C0169B85FA7D3BE0A91356004824A32E6C94803A1D005E6701B2B49D76A1257EC7310C2015E7C0151006E0843F8D000086C4284910A47518CF7DD04380553C2F2D4BFEE67350DE2C9331FEFAFAD24CB282004F328C73F4E8B49C34AF094802B2B004E76762F9D9D8BA500653EEA4016CD802126B72D8F004C5F9975200C924B5065C00686467E58919F960C017F00466BB3B6B4B135D9DB5A5A93C2210050B32A9400A9497D524BEA660084EEA8EF600849E21EFB7C9F07E5C34C014C009067794BCC527794BCC424F12A67DCBC905C01B97BF8DE5ED9F7C865A4051F50024F9B9EAFA93ECE1A49A2C2E20128E4CA30037100042612C6F8B600084C1C8850BC400B8DAA01547197D6370BC8422C4A72051291E2A0803B0E2094D4BB5FDBEF6A0094F3CCC9A0002FD38E1350E7500C01A1006E3CC24884200C46389312C401F8551C63D4CC9D08035293FD6FCAFF1468B0056780A45D0C01498FBED0039925B82CCDCA7F4E20021A692CC012B00440010B8691761E0002190E21244C98EE0B0C0139297660B401A80002150E20A43C1006A0E44582A400C04A81CD994B9A1004BB1625D0648CE440E49DC402D8612BB6C9F5E97A5AC193F589A100505800ABCF5205138BD2EB527EA130008611167331AEA9B8BDCC4752B78165B39DAA1004C906740139EB0148D3CEC80662B801E60041015EE6006801364E007B801C003F1A801880350100BEC002A3000920E0079801CA00500046A800C0A001A73DFE9830059D29B5E8A51865777DCA1A2820040E4C7A49F88028B9F92DF80292E592B6B840"
//    val input = "A0016C880162017C3686B18A3D4780"
    val binary = hexaToBinary(input)
    val packet = Packet(binary, 0)
    println(packet.value)
}

class Packet(
    private val binary: String,
    private val depth: Int
) {
    private val version: Int = binary.substring(0, 3).toInt(2)
    private val type: Int = binary.substring(3, 6).toInt(2)
    private val subPackets = mutableListOf<Packet>()
    var packetLength = 0
    val value = calculateValue(true)

    fun calculateValue(useVersion: Boolean): Long {
        val spaces = "  ".repeat(depth)
        val score = if (type == 4) {
            var keepChecking = '1'
            var binaryResult = ""
            var pointer = 6
            while (keepChecking == '1') {
                keepChecking = binary[pointer]
                binaryResult += binary.substring(pointer + 1, pointer + 5)
                pointer += 5
            }
            packetLength = pointer
            if (useVersion) {
                version.toLong()
            } else {
                println("${spaces}t: $type, s: ${binaryResult.toLong(2)}")
                binaryResult.toLong(2)
            }
        } else {
            println("${spaces}t: $type, ")
            val lengthType = binary[6]
            if (lengthType == '0') {
                val length = binary.substring(7, 22).toInt(2)
                packetLength = 22 + length
                var subPacketBinary = binary.substring(22, packetLength)
                while (subPacketBinary.isNotEmpty()) {
                    val sub = Packet(subPacketBinary, depth + 1)
                    subPackets += sub
                    subPacketBinary = subPacketBinary.drop(sub.packetLength)
                }
            } else {
                val count = binary.substring(7, 18).toInt(2)
                var subPacketBinary = binary.substring(18)
                for (i in 1..count) {
                    val sub = Packet(subPacketBinary, depth + 1)
                    subPackets += sub
                    subPacketBinary = subPacketBinary.drop(sub.packetLength)
                }
                packetLength = 18 + subPackets.sumOf { it.packetLength }
            }
            if (useVersion) {
                version + subPackets.sumOf { it.value }
            } else {
                calculateScore(type.toLong(), subPackets.map { it.value }.toMutableList(), false)
            }
        }
        return score
    }
}
fun hexaToBinary(input: String): String {
    var result = ""
    input.forEach {
        result += Integer.toBinaryString(Integer.valueOf(it.toString(), 16)).padStart(4, '0')
    }
    return result
}
fun calculateScore(type: Long, listScore: MutableList<Long>, versionSum: Boolean): Long {
    if (versionSum) return listScore.sum()
    return when (type.toInt()) {
        0 -> listScore.sum()
        1 -> listScore.reduce { acc, add -> acc * add }
        2 -> listScore.minOf { it }
        3 -> listScore.maxOf { it }
        5 -> if (listScore[0] > listScore[1]) 1 else 0
        6 -> if (listScore[0] < listScore[1]) 1 else 0
        7 -> if (listScore[0] == listScore[1]) 1 else 0
        else -> 1
    }
}
