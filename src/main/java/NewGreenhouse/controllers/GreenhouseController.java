package NewGreenhouse.controllers;

import NewGreenhouse.Phvalues;
import NewGreenhouse.PhvaluesDAODB;
import NewGreenhouse.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
public class GreenhouseController {

    PhvaluesDAODB phvaluesDAODB = new PhvaluesDAODB();

    public GreenhouseController() {
    }

    @RequestMapping("/db/getlatestvalues")
    public Phvalues getLatestValues() throws SQLException {
        return phvaluesDAODB.getLatestValues();
    }

    @RequestMapping("/db/getlatestweekvalues")
    public List<Phvalues> getLatestWeekValues() throws SQLException, IOException, ClassNotFoundException {
        return phvaluesDAODB.GetLatestWeekValues();
    }


    @PostMapping("/db/phvalues/add")
    public Response addPhvalues(@RequestBody Phvalues p) throws SQLException, IOException, ClassNotFoundException {
        Response res = new Response("System updated", Boolean.FALSE);
        Boolean success = phvaluesDAODB.addPhvalues(p);
        res.setStatus(success); // Set till bool var:en ovan
        return res;
    }
}
