/*
 * Copyright 2017 KoFuk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chronoscoper.ftpupload

import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPReply
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream

fun main(args: Array<String>) {
  if (args.size < 5) {
    println("host user password localFile remoteFile")
    return
  }
  val host = args[0]
  val username = args[1]
  val password = args[2]
  val local = args[3]
  val remote = args[4]
  val client = FTPClient()
  client.controlEncoding = "UTF-8"
  client.connect(host)
  if (!FTPReply.isPositiveCompletion(client.replyCode)) {
    throw RuntimeException("Failed to connect to host")
  }
  client.login(username, password)
  if (!FTPReply.isPositiveCompletion(client.replyCode)) {
    throw RuntimeException("Failed to login")
  }
  client.setFileType(FTP.BINARY_FILE_TYPE)
  BufferedInputStream(FileInputStream(File(local))).use { fileStream ->
    client.storeFile(remote, fileStream)
  }
  client.logout()
  client.disconnect()
}
