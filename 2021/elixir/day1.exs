defmodule AoC2021.Day1 do
  def run(path) do
    read_depths(path)
    |> count_depth_increases()
    |> IO.puts()
  end

  def count_depth_increases(depths) do
    do_cdi(0, depths)
  end

  def read_depths(path) do
    File.stream!(path)
    |> Stream.map(&String.trim/1)
    |> Enum.map(&String.to_integer/1)
  end

  defp do_cdi(n, [_ | []]) do
    n
  end

  defp do_cdi(n, [current_depth | tail]) do
    if current_depth < hd(tail) do
      do_cdi(n + 1, tail)
    else
      do_cdi(n, tail)
    end
  end
end

AoC2021.Day1.run("./data/day1.txt")
