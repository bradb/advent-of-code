defmodule AoC.Day2 do
  def part1(path) do
    input = File.stream!(path)

    %{d: d, h: h} =
      input
      |> Enum.map(&String.trim/1)
      |> Enum.map(fn s -> String.split(s, " ") end)
      |> Enum.reduce(
        %{d: 0, h: 0},
        fn [direction, step], acc ->
          case direction do
            "forward" ->
              Map.update!(acc, :h, fn v -> v + String.to_integer(step) end)

            "down" ->
              Map.update!(acc, :d, fn v -> v + String.to_integer(step) end)

            "up" ->
              Map.update!(acc, :d, fn v -> v - String.to_integer(step) end)
          end
        end
      )

    IO.puts(d * h)
  end

  def part2(path) do
    input = File.stream!(path)

    %{d: d, h: h} =
      input
      |> Enum.map(&String.trim/1)
      |> Enum.map(fn s -> String.split(s, " ") end)
      |> Enum.reduce(
        %{d: 0, h: 0, a: 0},
        fn [direction, step], acc = %{a: a} ->
          case direction do
            "forward" ->
              Map.update!(acc, :h, fn v -> v + String.to_integer(step) end)
              |> Map.update!(:d, fn v -> v + a * String.to_integer(step) end)

            "down" ->
              Map.update!(acc, :a, fn v -> v + String.to_integer(step) end)

            "up" ->
              Map.update!(acc, :a, fn v -> v - String.to_integer(step) end)
          end
        end
      )

    IO.puts(d * h)
  end
end

AoC.Day2.part1("./data/day2.txt")
AoC.Day2.part2("./data/day2.txt")
