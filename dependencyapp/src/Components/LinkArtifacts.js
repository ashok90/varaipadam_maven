import React, { Component } from 'react'
import axios from 'axios';

class LinkArtifacts extends Component {
    constructor(props) {
        super(props);
        this.state = {
            childJar: '',
            parentJar: '',
            message: ''
        };

        this.handleChildChange = this.handleChildChange.bind(this);
        this.handleParentChange = this.handleParentChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChildChange(event) {
        this.setState({ childJar: event.target.value });
    }

    handleParentChange(event) {
        this.setState({ parentJar: event.target.value });
    }

    clearForm() {
        this.setState({
            childJar: '',
            parentJar: ''
        });
    }

    handleSubmit(event) {
        var self = this;
        console.log('A name was submitted: ' + this.state.value);
        event.preventDefault();
        axios.get('http://localhost:9090/link_artifacts', {
            params: {
                childJar: this.state.childJar,
                parentJar: this.state.parentJar
            }
        })
            .then(function (response) {
                self.setState({ message: response.data });
                console.log(response);
            })
            .catch(function (error) {
                console.log(error);
            });
        this.clearForm();
    }


    render() {
        return (
            <div className="container">
            <br/><br/>
                <div align="center">
                    <h3>Link Artifacts</h3><br/><br/>
                    <form onSubmit={this.handleSubmit}>
                        <table>
                            <tbody>
                                <tr>
                                    <td>
                                        <label>Child Name Jar: &nbsp;</label>
                                    </td>
                                    <td>
                                        <input type="text" value={this.state.childJar} onChange={this.handleChildChange} />
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <label>Parent Jar Name: &nbsp;</label>
                                    </td>
                                    <td>
                                        <input type="text" value={this.state.parentJar} onChange={this.handleParentChange} />
                                    </td>
                                </tr>
                                <br/><br/>
                                <tr align="right">
                                    <td>
                                        <input type="submit" value="Submit" />
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </form>
                </div>
                <div>
                    <p>{this.state.message}</p>
                </div>
            </div>
        )
    }
}

export default LinkArtifacts
