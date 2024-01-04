# MIT License
# 
# Copyright (c) 2022-2023 The Railways Team
# 
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in all
# copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
# SOFTWARE.
#
# https://github.com/Layers-of-Railways/MinecraftGraphicsScripts/blob/master/obj_uniqueify/obj_uniqueify.py

# Verify that all models with a "loader": "forge:obj" entry also have a "porting_lib:loader": "porting_lib:obj" entry
from glob import glob
from typing import Final

import os
import json

os.chdir("..")

directories: Final[list[str]] = [
    "common/src/main/resources/assets/extendedbogeys/models",
    "common/src/generated/resources/assets/extendedbogeys/models"
]

def needs_loader(data: dict) -> bool:
    return "loader" in data and data["loader"] == "forge:obj" and "porting_lib:loader" not in data

def fix_files():
    for directory in directories:
        for file in glob(f"{directory}/**/*.json", recursive=True):
            fix_file(file)

def fix_file(file: str) -> None:
    file_data = read_file(file)
    
    if needs_loader(file_data):
        file_data["porting_lib:loader"] = "porting_lib:obj"
        write_file(file, file_data)
        print(f"Fixed {file}")

def read_file(path: str) -> dict:
    with open(path, 'r') as file:
        return json.load(file)

def write_file(path: str, data: dict) -> None:
    with open(path, 'w') as file:
        json.dump(data, file, indent=4)

if __name__ == '__main__':
    fix_files()