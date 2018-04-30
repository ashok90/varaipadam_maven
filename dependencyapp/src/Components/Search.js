import React, { Component } from 'react'
import axios from 'axios';

class Search extends Component {
    constructor(props) {
        super(props);
        this.state = { 
            value: '',
            nodes: [],
            hasData: false
         };

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event) {
        this.setState({ value: event.target.value });
    }

    clearForm() {
        this.setState({ value: '' });
    }

    handleSubmit(event) {
        var self = this;
        console.log('A name was submitted: ' + this.state.value);
        event.preventDefault();
        axios.get('http://localhost:9090/get_dependencies', {
            params: {
                name: this.state.value
            }
        })
            .then(function (response) {
                console.log(response.data.nodes);
                self.setState({ nodes: response.data.nodes });
            })
            .catch(function (error) {
                console.log(error);
            });
        this.clearForm();
    }


    render() {
        const child = this.state.nodes.map(data => {
            return <tr key={data.group}>
            <td>{ data.name }</td>
            <td>{ data.group_id }</td>
            <td>{ data.artifact_id }</td>
            <td>{ data.version }</td>
            <td>{ data.no_of_dependencies }</td>
          </tr>
          });

        return (
            <div className="container">
            <br/><br/><br/>
                <div align="center">
                    <form onSubmit={this.handleSubmit}>
                        <label>
                            Enter jar name: &nbsp;
                        <input type="text" value={this.state.value} onChange={this.handleChange} />
                        </label>
                        &nbsp;
                        <input type="submit" value="Submit" />
                    </form>
                </div>
                <div align="center">
                    <br/>
                    <br/>
                    <table border="1">
                        <tbody>
                            <tr>
                                <th>Name</th>
                                <th>Group Id</th>
                                <th>Artifact Id</th>
                                <th>Version</th>
                                <th>Dependencies</th>
                            </tr>
                        {child}
                        </tbody>
                    </table>
                </div>
            </div>
        )
    }
}

export default Search
