import React, { Component } from "react";

class Home extends Component {
    render() {
        return (
            <div className="container">
                <div className="heading"> <h3 align="center">Maven Dependency Management</h3> </div>
                <div className="row">
                    <div className="col-md-3"></div>
                    <div className="col-md-8 img-container">
                        <img src={require("./data-flow.jpeg")}></img>
                    </div>
                </div>
                {/* <div className="row">
                    <div className="col-md-1"></div>
                    <div className="col-md-3">
                        <div className="tech-top" align="center"><b>Neo4j</b></div>
                        <div className="tech-stack">
                            Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
                            </div>
                    </div>
                    <div className="col-md-3">
                        <div className="tech-top" align="center"><b>Eclipse Vert.x</b></div>
                        <div className="tech-stack">
                            Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
                        </div>
                    </div>
                    <div className="col-md-3">
                        <div className="tech-top" align="center"><b>Reactjs</b></div>
                        <div className="tech-stack">
                            Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
                        </div>
                    </div>
                </div> */}
                <div className="row">
                    <div className="description">
                        <div align="center"><b>Project Description</b></div><br />
                        We present graph based approach for maven dependency management.
                        This is an end-to-end data pipeline project that includes data acquisition, processing,
                        consumption with search capability and visualization.  The focus of this project is to explore
                        and demonstrate how maven dependencies can be better managed with graph database  and demonstrate
                        how an API layer can be created using Vert.x reactive framework on top of Neo4j.
                        We explain the implementation of each of the steps in detail and provide our results.
                        This is an academic project as part of the course SP18: SQL AND NOSQL: 14149 at Indiana University.
                        </div>
                </div>
                <br />
                <br />
                <div className="row ">
                    <div className="col-md-3">
                    </div>
                    <div className="col-md-3 team-details">
                        <b>Balaji Rajaram</b><br />
                        Graduate Student, MS in Data Science<br />
                        Indiana University at Bloomington<br />
                        brajaram@iu.edu
                        </div>
                    <div className="col-md-3 team-details">
                        <b>Ashok Kuppuraj</b><br />
                        Graduate Student, MS in Data Science<br />
                        Indiana University at Bloomington<br />
                        akuppura@iu.edu
                        </div>
                    <div className="col-md-3">
                    </div>
                </div>
            </div>
        );
    }
}

export default Home;