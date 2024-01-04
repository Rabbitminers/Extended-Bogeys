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

from typing import Final
import os

INPUT_DIR: Final[str] = "input"
OUTPUT_DIR: Final[str] = "output"

def obj_uniqueify(input_path: str, output_path: str) -> None:
    counts: dict[str, int] = {}
    dat = read_file(input_path)

    out = []
    for line in dat.split("\n"):
        line = line.strip()

        if line.startswith("o "):
            if "_" in line:
                try:
                    int(line.split("_")[-1])
                    out.append(line)
                    continue
                except ValueError:
                    pass
        
            object_name = line.removeprefix("o ").strip()
            idx = counts.get(object_name, 0)
        
            counts[object_name] = idx + 1
            out.append(f"o {object_name}_{idx}")
        else:
            out.append(line)

    with open(output_path, "w") as f:
        f.write("\n".join(out))

def read_file(input_path: str) -> str:
    with open(input_path) as f:
        return f.read()

def main() -> None:
    for name in os.listdir(INPUT_DIR):
        obj_uniqueify(f"{INPUT_DIR}/{name}", f"{OUTPUT_DIR}/{name}")

if __name__ == "__main__":
    main()