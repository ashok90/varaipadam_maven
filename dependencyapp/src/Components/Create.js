import React, { Component } from 'react'
import axios from 'axios';

class Create extends Component {
    constructor(props) {
        super(props);
        this.state = {
            name: '',
            groupId: '',
            artifactId: '',
            version: '',
            noOfDependencies: '',
            message: ''
        };

        this.handleNameChange = this.handleNameChange.bind(this);
        this.handleGroupChange = this.handleGroupChange.bind(this);
        this.handleArtifactChange = this.handleArtifactChange.bind(this);
        this.handleVersionChange = this.handleVersionChange.bind(this);
        this.handleNumberChange = this.handleNumberChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleNameChange(event) {
        this.setState({ name: event.target.value });
    }

    handleGroupChange(event) {
        this.setState({ groupId: event.target.value });
    }

    handleArtifactChange(event) {
        this.setState({ artifactId: event.target.value });
    }

    handleVersionChange(event) {
        this.setState({ version: event.target.value });
    }

    handleNumberChange(event) {
        this.setState({ noOfDependencies: event.target.value });
    }

    clearForm() {
        this.setState({
            name: '',
            groupId: '',
            artifactId: '',
            version: '',
            noOfDependencies: ''
        });
    }

    handleSubmit(event) {
        var self = this;
        console.log('A name was submitted: ' + this.state.value);
        event.preventDefault();
        axios.get('http://localhost:9090/add_artifact', {
            params: {
                name: this.state.name,
                groupId: this.state.groupId,
                artifactId: this.state.artifactId,
                version: this.state.version,
                noOfDependencies: this.state.noOfDependencies
            }
        })
            .then(function (response) {
                console.log(response);
                self.setState({ message: response.data });
            })
            .catch(function (error) {
                console.log(error);
            });
        this.clearForm();
    }


    render() {
        return (
            <div className="container">
                <br/>
                <br/>
                <div align="center">
                    <h3>Node creation</h3><br/><br/>
                    <form onSubmit={this.handleSubmit}>
                        <table>
                            <tbody>
                                <tr>
                                    <td>
                                        <label>Jar name: &nbsp;</label>
                                    </td>
                                    <td>
                                        <input type="text" value={this.state.name} onChange={this.handleNameChange} />
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <label>Group Id: &nbsp;</label>
                                    </td>
                                    <td>
                                        <input type="text" value={this.state.groupId} onChange={this.handleGroupChange} />
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <label>Artifact Id: &nbsp;</label>
                                    </td>
                                    <td>
                                        <input type="text" value={this.state.artifactId} onChange={this.handleArtifactChange} />
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <label>Version: &nbsp;</label>
                                    </td>
                                    <td>
                                        <input type="text" value={this.state.version} onChange={this.handleVersionChange} />
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <label>Number of dependencies: &nbsp;</label>
                                    </td>
                                    <td>
                                        <input type="text" value={this.state.noOfDependencies} onChange={this.handleNumberChange} />
                                    </td>
                                </tr>
                                <br/>
                                <br/>
                                <tr align="right">
                                    <td>
                                        <input type="submit" value="Submit" />
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </form>
                </div>
                <br/>
                <br/>
                <div>
                    <p>{this.state.message}</p>
                </div>
            </div>
        )
    }
}

export default Create
