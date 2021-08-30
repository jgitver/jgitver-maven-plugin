#!/bin/bash
#
# Copyright (C) 2016 Matthieu Brouillard [http://oss.brouillard.fr/jgitver-maven-plugin] (matthieu@brouillard.fr)
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#         http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# # # # # # # # # # # # # # # # # # # # 
# this build:
#   - first run the IT tests in place in a sub directory of current project
#     meaning that a .mvn/extensions.xml file will always be found, testing the default ignore settings of current project
#     when there are refactoring in the way exclusions are handled and as a build run on CI
#   - if the first run has failed, relaunch a build by externalizing the integration test directory in a safe place
#     without any interference of the jgitver-maven-plugin version referenced by the current .mvn/extensions.xml file
# # # # # # # # # # # # # # # # # # # # 
mvn -Prun-its clean install || mvn -Prun-its clean install -Dit.directory=/tmp/jgitver-it