defmodule AoC2021.Day1 do
  def part1(path) do
    read_depths(path)
    |> count_depth_increases()
    |> IO.puts()
  end

  def part2(path) do
    read_depths(path)
    |> sliding_windows(3)
    |> count_depth_increases()
    |> IO.puts()
  end

  def sliding_windows(xs, n) do
    do_sw(xs, n, [])
  end

  defp do_sw([], _, windows) do
    Enum.reverse(windows)
  end

  defp do_sw(xs, n, windows) do
    w = Enum.take(xs, n)

    if Enum.count(w) == n do
      do_sw(tl(xs), n, [Enum.sum(w) | windows])
    else
      do_sw([], n, windows)
    end
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

AoC2021.Day1.part1("./data/day1.txt")
AoC2021.Day1.part2("./data/day1.txt")
