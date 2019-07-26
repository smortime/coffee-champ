package com.smort.swagger

import com.github.swagger.akka.SwaggerHttpService
import com.github.swagger.akka.model.Info
import com.smort.routes.RestApi

object SwaggerDocService extends SwaggerHttpService {
  override val apiClasses: Set[Class[_]] = Set(classOf[RestApi])
  override val host = "localhost:8080" //the url of your api, not swagger's json endpoint
  override val apiDocsPath = "api-docs" //where you want the swagger-json endpoint exposed
  override val info = Info(version = "1.0") //provides license and other description details
}
