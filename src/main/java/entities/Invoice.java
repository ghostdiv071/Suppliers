package entities;

import lombok.*;

import java.sql.Date;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Invoice {

    private int id;

    @NonNull
    private Date date;

    @NonNull //NonNull нужен для RequiredArgsConstructor
    private int organisation_id;
}

