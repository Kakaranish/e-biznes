import React, { useEffect, useState } from 'react';
import { useHistory } from 'react-router-dom';
import axios from 'axios';
import { getFormDataJsonFromEvent } from '../../common';

const RegisterPage = () => {

    const onSubmit = async event => {
        event.preventDefault();
        let formData = getFormDataJsonFromEvent(event);
        if (formData.password !== formData.repeatPassword) {
            setValidationErrors(["passwords are different"])
            return;
        }

        formData.repeatPassword = undefined;
        const result = await axios.post('/auth/register', formData, {
            headers: { 'X-Auth-Token': localStorage.getItem('token') },
            validateStatus: false
        });
        if(result.status !== 200) {
            setValidationErrors([result.data ?? "Unknown error"])
            return;
        }

        setValidationErrors(null);
        localStorage.setItem('token', result.data.token);
        history.push('/');
    }

    const history = useHistory();

    const [validationErrors, setValidationErrors] = useState(null);

    useEffect(() => {
        const verifyIfLogged = async () => {
            const token = localStorage.getItem('token');
            if (!token) return;
            const result = await axios.post('/auth/verify', {}, {
                headers: { 'X-Auth-Token': localStorage.getItem('token') },
                validateStatus: false
            });
            if (result.status === 200) {
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
                <input name="firstName" type="text" className="form-control"
                    id="firstNameInput" placeholder="First name..." required />
            </div>
            <div className="form-group">
                <input name="lastName" type="text" className="form-control"
                    id="lastNameInput" placeholder="Last name..." required />
            </div>
            <div className="form-group">
                <input name="password" type="password" className="form-control"
                    id="passwordInput" placeholder="Password..." required />
            </div>

            <div className="form-group">
                <input name="repeatPassword" type="password" className="form-control"
                    id="repepatPasswordInput" placeholder="Repeat password..." required />
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

export default RegisterPage;