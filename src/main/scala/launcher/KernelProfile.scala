package launcher

import java.io.{File, BufferedWriter, FileWriter}
import java.net.ServerSocket
import javax.crypto.Mac
import java.util.UUID.randomUUID
import javax.crypto.spec.SecretKeySpec
import net.liftweb.json.Extraction.decompose
import net.liftweb.json.Printer.pretty
import net.liftweb.json.render


class KernelProfile(kernelID : String, kernelName : String, ip : String = "0.0.0.0") {

  var hb_port = 0
  var control_port = 0
  var iopub_port = 0
  var stdin_port = 0
  var shell_port = 0
  var key = ""

  def initPort() : Int = new ServerSocket(0).getLocalPort

  def initAllPorts() = {
    hb_port = initPort()
    control_port = initPort()
    iopub_port = initPort()
    stdin_port = initPort()
    shell_port = initPort()
  }

  // self.session.key
  def generateKey(sharedSecret: String, preHashString: String, hash : String = "HmacSHA256") = {
    val uuid = randomUUID.toString
    val secret = new SecretKeySpec(sharedSecret.getBytes, hash)
    val mac = Mac.getInstance(hash)
    mac.init(secret)
    val hashString: Array[Byte] = mac.doFinal(preHashString.getBytes)
    key = new String(hashString.map(_.toChar))
    key = ""
  }

  def saveAsJson(outputDir : String, fileName : String = null) : String = {
    var outputPath = outputDir
    if(fileName == null || fileName.trim.length > 0){
      outputPath += "/kernel-%s.json".format(kernelID)
    } else{
      outputPath += "/%s".format(fileName)
    }
    val file = new File(outputPath)
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write(toJson)
    bw.close()
    outputPath
  }

  def toJson : String = {
    val map = Map(
      "hb_port" -> hb_port,
      "control_port" -> control_port,
      "iopub_port" -> iopub_port,
      "stdin_port" -> stdin_port,
      "shell_port" -> shell_port,
      "key" -> key,
      "signature_scheme" -> "hmac-sha256",
      "transport" -> "tcp",
      "ip" -> ip
    )
    implicit val formats = net.liftweb.json.DefaultFormats // Used for decompose
    pretty(render(decompose(map)))
  }

  def createProfile(outputDir : String,
                    fileName : String = null,
                    newKey : Boolean = false) : String = {
    initAllPorts()
    if (newKey){ generateKey(null, null) }
    saveAsJson(outputDir, fileName)
  }

}
