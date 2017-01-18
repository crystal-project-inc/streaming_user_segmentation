package com.crystal

// akka
import akka.actor.{ ActorSystem, Actor, Props }

// Spark
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{ Duration, StreamingContext }

object Main extends App {
  AppConfig.setArgs(args)


  AppConfig.load() match {
    case Some(appConfig) =>
      val sparkConf = new SparkConf()
        .setMaster("local[*]")
        .setAppName(appConfig.appName)

      val streamingCtx = new StreamingContext(
        sparkConf,
        Duration(appConfig.checkpointInterval)
      )

      val system = ActorSystem("SegmentationSystem")
      val overseer = system.actorOf(Overseer.props(appConfig, streamingCtx), "overseer")
    case None => ()
  }
}
