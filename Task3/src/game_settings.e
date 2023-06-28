class
    GAME_SETTINGS

create
    make
feature

    rows: INTEGER
    cols: INTEGER
    show_pipes_color: BOOLEAN
    pipe_colors: ARRAY[CHARACTER]

    subway_systems: INTEGER
    max_subway_exits: INTEGER
    mouses: INTEGER

    make
    do
        rows := 10
        cols := 10
        show_pipes_color := True

        pipe_colors := <<'o', 'x', 'u', 'b'>>
        subway_systems := 4
        max_subway_exits := 4
        mouses := 5
    end

feature -- Element change

    toggle_show_pipes_color
        do
            show_pipes_color := not show_pipes_color
        end


invariant
    positive_rows: rows > 0
    positive_cols: cols > 0

    subway_systems_have_colors: pipe_colors.count >= subway_systems
    positive_subway_systems : subway_systems > 0
    positive_max_subway_exits : max_subway_exits > 2
    positive_mouses: mouses > 0

end