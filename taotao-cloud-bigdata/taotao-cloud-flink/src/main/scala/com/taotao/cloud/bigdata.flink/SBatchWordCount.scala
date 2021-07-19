/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.bigdata.flink

import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, createTypeInformation}

/**
 * SBatchWordCount
 *
 * @author shuigedeng
 * @since 2020/11/3 09:05
 * @version 1.0.0
 */
object SBatchWordCount {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    val lines = env.readTextFile("/Users/shuigedeng/spark/hello.txt")

    lines.flatMap(_.split(" "))
      .map((_, 1))
      .keyBy(_._1)
      .sum(1)
      .print()

    Thread.sleep(Long.MaxValue)

    env.execute("SBatchWordCount")

  }
}
