import React, { Component } from 'react'
import axios from 'axios';

class Delete extends Component {
    constructor(props) {
        super(props);
        this.state = {
            name: '',
            message: ''
        };

        this.handleNameChange = this.handleNameChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleNameChange(event) {
        this.setState({ name: event.target.value });
    }

    clearForm() {
        this.setState({
            name: ''
        });
    }

    handleSubmit(event) {
        var self = this;
        console.log('A name was submitted: ' + this.state.value);
        event.preventDefault();
        axios.get('http://localhost:9090/delete_artifact', {
            params: {
                name: this.state.name
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
            <div align="container">
                <div align="center">
                <br/><br/>
                    <h3>Delete node & it's relationship</h3><br/><br/>
                    <form onSubmit={this.handleSubmit}>
                        <label>Enter jar name to delete: &nbsp;</label>
                        <input type="text" value={this.state.name} onChange={this.handleNameChange} />
                        &nbsp;
            <input type="submit" value="Submit" />
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

export default Delete
