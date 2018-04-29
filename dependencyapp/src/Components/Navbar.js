import React, { Component } from "react";
import {
    Route,
    BrowserRouter
} from "react-router-dom";
import Home from "./Home";
import Search from "./Search";
import Create from "./Create";
import Delete from "./Delete";
import LinkArtifacts from "./LinkArtifacts";

class Navbar extends Component {
    render() {
        return (
            <BrowserRouter>
                <div>
                    <nav className="navbar navbar-inverse">
                        <div className="container-fluid">
                            {/* <div class="navbar-header">
                                <a class="navbar-brand" href="#">Dependency Management</a>
                            </div> */}
                            <ul className="nav navbar-nav">
                                <li><a href="/">Home</a></li>
                                <li><a href="/create">Create</a></li>
                                <li><a href="/search">Search</a></li>
                                <li><a href="/delete">Delete</a></li>
                                <li><a href="/link">Link</a></li>
                            </ul>
                        </div>
                    </nav>
                    <div className="content">
                        <Route exact path="/" component={Home} />
                        <Route path="/create" component={Create} />
                        <Route path="/delete" component={Delete} />
                        <Route path="/search" component={Search} />
                        <Route path="/link" component={LinkArtifacts} />
                    </div>
                </div>
            </BrowserRouter>
        );
    }
}

export default Navbar;