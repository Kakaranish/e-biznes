import React, { useState, useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import axios from 'axios';
import { getFormDataJsonFromEvent } from '../../common';

const LoginPage = () => {

    const history = useHistory();

    const [validationErrors, setValidationErrors] = useState(null);

    const onSubmit = async event => {
        event.preventDefault();
        const formData = getFormDataJsonFromEvent(event);
        const result = await axios.post('/auth/login', formData, { validateStatus: false });
        if (result.status !== 200) {
            setValidationErrors([result.data.msg]);
            return;
        }
        localStorage.setItem('token', result.data.token);

        history.push('/')
    }

    useEffect(() => {
        const verifyIfLogged = async () => {
            const token = localStorage.getItem('token');
            if (!token) return;
            const result = await axios.post('/auth/verify', {}, {
                headers: {
                    'X-Auth-Token': localStorage.getItem('token')
                },
                validateStatus: false
            });
            if(result.status === 200) {
                alert("You' are already logged in");
                history.push('/');
                return;
            }
            localStorage.removeItem('token');
        };
        verifyIfLogged();
    }, []);

    return <>
        <h3>Sign In</h3>
        <form onSubmit={onSubmit}>
            <div className="form-group">
                <input name="email" type="email" className="form-control" id="emailInput" placeholder="Email..." required />
            </div>
            <div className="form-group">
                <input name="password" type="password" className="form-control" id="passwordInput" placeholder="Password..." required />
            </div>

            <button type="submit" className="btn btn-primary">
                Submit
            </button>

            {
                validationErrors &&
                <div className="col-12 mt-2">
                    <p className="text-danger font-weight-bold" style={{ marginBottom: '0px' }}>
                        Validation errors
                        </p>
                    <ul style={{ paddingTop: "0" }, { marginTop: "0px" }}>
                        {
                            validationErrors.map((error, i) => {
                                return <li key={`val-err-${i}`} className="text-danger">{error}</li>
                            })
                        }
                    </ul>
                </div>
            }
        </form>
    </>
};

export default LoginPage;