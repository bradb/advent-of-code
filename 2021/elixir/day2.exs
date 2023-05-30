defmodule AoC.Day2 do
  def part1(path) do
    %{d: d, h: h} =
      File.stream!(path)
      |> Stream.map(&String.trim/1)
      |> Stream.map(fn s -> String.split(s, " ") end)
      |> Enum.reduce(
        %{d: 0, h: 0},
        fn [direction, step], %{h: h, d: d} ->
          step = String.to_integer(step)

          case direction do
            "forward" -> %{h: h + step, d: d}
            "down" -> %{h: h, d: d + step}
            "up" -> %{h: h, d: d - step}
          end
        end
      )

    IO.puts(d * h)
  end

  def part2(path) do
    %{d: d, h: h} =
      File.stream!(path)
      |> Stream.map(&String.trim/1)
      |> Stream.map(fn s -> String.split(s, " ") end)
      |> Enum.reduce(
        %{d: 0, h: 0, a: 0},
        fn [direction, step], %{d: d, h: h, a: a} ->
          step = String.to_integer(step)

          case direction do
            "forward" -> %{d: d + a * step, h: h + step, a: a}
            "down" -> %{d: d, h: h, a: a + step}
            "up" -> %{d: d, h: h, a: a - step}
          end
        end
      )

    IO.puts(d * h)
  end
end

AoC.Day2.part1("./data/day2.txt")
AoC.Day2.part2("./data/day2.txt")
